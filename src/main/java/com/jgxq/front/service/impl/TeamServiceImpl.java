package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.TagSearchRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.DeleteEnum;
import com.jgxq.front.entity.Team;
import com.jgxq.front.mapper.TeamMapper;
import com.jgxq.front.mapper.UserMapper;
import com.jgxq.front.sender.EsUtils;
import com.jgxq.front.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EsUtils esClient;

    @Override
    public Page<Team> pageTeams(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.select("id","name","logo");

        teamMapper.selectPage(page,null);

        return page;
    }

    @Override
    public Page<TeamBasicRes> searchTeam(Integer pageNum, Integer pageSize, String keyword) {
        return pageTeamEs(pageNum,pageSize,keyword);//es搜索
//        Page page = new Page(pageNum, pageSize);//fixme sql
//        page(page,new QueryWrapper<Team>().like("`name`",keyword).orderByAsc("LENGTH(`name`)"));
//        List<Team> records = page.getRecords();
//        List<TeamBasicRes> res = new ArrayList<>(records.size());
//        records.forEach((team) -> {
//            TeamBasicRes teamBasic = new TeamBasicRes();
//            BeanUtils.copyProperties(team, teamBasic);
//            res.add(teamBasic);
//        });
//        Page<TeamBasicRes> resPage = new Page<>(page.getCurrent(),page.getSize(),page.getTotal());
//        resPage.setRecords(res);
//        return resPage;
    }

    private Page<TeamBasicRes> pageTeamEs(Integer pageNum, Integer pageSize, String keyword){
        SearchSourceBuilder builder = new SearchSourceBuilder();

        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("name.pinyin", keyword);
        TermQueryBuilder termQuery1 = QueryBuilders.termQuery("status", 1);
        TermQueryBuilder termQuery2 = QueryBuilders.termQuery("type",0);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchPhraseQuery);
        boolQueryBuilder.must(termQuery1);
        boolQueryBuilder.must(termQuery2);

        builder.query(boolQueryBuilder);
        Page<TagSearchRes> page = esClient.search("jgxq_tag", builder, TagSearchRes.class, pageNum, pageSize);
        Page resPage = new Page<TeamBasicRes>(page.getCurrent(),page.getSize(),page.getTotal());
        resPage.setRecords(page.getRecords().stream().map(t->{
            TeamBasicRes teamBasicRes = new TeamBasicRes();
            BeanUtils.copyProperties(t,teamBasicRes);
            teamBasicRes.setId(t.getObjectId());
            return teamBasicRes;
        }).collect(Collectors.toList()));
        return resPage;
    }

    @Override
    public Page<Team> pageTeamsByHeat(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Team> userQuery = new QueryWrapper<>();
        userQuery.select("id","name","logo"
                ,"(SELECT count(id) from user as u where team.id = u.home_team) as fans ")
                .orderByDesc("fans").orderByAsc("id");
        teamMapper.selectPage(page, userQuery);
        return page;
    }

    @Override
    public TeamBasicRes getBasicTeamById(Integer id) {
        Team team = teamMapper.selectById(id);
        if(team == null){
            return null;
        }
        TeamBasicRes teamRes = new TeamBasicRes();
        BeanUtils.copyProperties(team,teamRes);
        return teamRes;
    }

    public List<TeamBasicRes> getBasicTeamByIds(Collection<Integer> ids){

        if(ids.isEmpty()) return Collections.emptyList();

        QueryWrapper<Team> teamQuery = new QueryWrapper<>();
        teamQuery.select("id","name","logo").in("id",ids);
        List<Team> team = teamMapper.selectList(teamQuery);
        List<TeamBasicRes> res = team.stream().map(t -> {
            TeamBasicRes teamBasicRes = new TeamBasicRes();
            BeanUtils.copyProperties(t, teamBasicRes);
            return teamBasicRes;
        }).collect(Collectors.toList());
        return res;
    }
}
