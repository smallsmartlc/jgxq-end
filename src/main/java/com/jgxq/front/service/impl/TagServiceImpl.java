package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.PlayerBasicRes;
import com.jgxq.common.res.TagRes;
import com.jgxq.common.res.TagSearchRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.Tag;
import com.jgxq.front.mapper.TagMapper;
import com.jgxq.front.sender.EsUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jgxq.front.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private EsUtils esClient;

    @Override
    public TagRes getTags(Integer newsId){
        QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
        tagQuery.eq("news_id",newsId);
        List<Tag> tags = tagMapper.selectList(tagQuery);

        Map<Integer, List<Tag>> map = tags.stream().collect(Collectors.groupingBy(tag -> tag.getObjectType().intValue()));
        TagRes tagRes = new TagRes();
        List<Tag> teams = map.get(TagType.TEAM.getValue());
        List<Tag> players = map.get(TagType.PLAYER.getValue());
        if(teams!=null){
            List<Integer> teamIds = teams.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<TeamBasicRes> teamList = teamService.getBasicTeamByIds(teamIds);
            tagRes.setTeams(teamList);
        }
        if(players!=null){
            List<Integer> playerIds = players.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<PlayerBasicRes> playerList = playerService.getBasicByIds(playerIds);
            tagRes.setPlayers(playerList);
        }
        return tagRes;
    }

    @Override
    public List<TagSearchRes> getTagList(Integer newsId){
        QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
        tagQuery.eq("news_id",newsId);
        List<Tag> tags = tagMapper.selectList(tagQuery);

        Map<Integer, List<Tag>> map = tags.stream().collect(Collectors.groupingBy(tag -> tag.getObjectType().intValue()));
        List<TagSearchRes> tagRes = new ArrayList<>();
        List<Tag> teams = map.get(TagType.TEAM.getValue());
        List<Tag> players = map.get(TagType.PLAYER.getValue());
        if(teams!=null){
            List<Integer> teamIds = teams.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<TeamBasicRes> teamList = teamService.getBasicTeamByIds(teamIds);
            List<TagSearchRes> teamTags = teamList.stream().map(t -> {
                TagSearchRes tag = new TagSearchRes();
                tag.setType(TagType.TEAM.getValue());
                tag.setObjectId(t.getId());
                tag.setLogo(t.getLogo());
                tag.setName(t.getName());
                return tag;
            }).collect(Collectors.toList());
            tagRes.addAll(teamTags);
        }
        if(players!=null){
            List<Integer> playerIds = players.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<PlayerBasicRes> playerList = playerService.getBasicByIds(playerIds);
            List<TagSearchRes> playerTags = playerList.stream().map(p -> {
                TagSearchRes tag = new TagSearchRes();
                tag.setType(TagType.PLAYER.getValue());
                tag.setObjectId(p.getId());
                tag.setLogo(p.getHeadImage());
                tag.setName(p.getName());
                return tag;
            }).collect(Collectors.toList());
            tagRes.addAll(playerTags);
        }
        return tagRes;
    }

    @Override
    public List<TagSearchRes> searchTag(String keyword) {
//        return searchTagByEs(keyword);
        return tagMapper.searchTag(keyword);//fixme 使用Sql做搜索
    }

    @Override
    public Page<TagSearchRes> pageTag(Integer pageNum, Integer pageSize, String keyword) {
//        return pageTeamEs(pageNum, pageSize, keyword);//es
        return tagMapper.pageTag(new Page<>(pageNum,pageSize),keyword);//fixme sql
    }

    private Page<TagSearchRes> pageTeamEs(Integer pageNum, Integer pageSize, String keyword){
        SearchSourceBuilder builder = new SearchSourceBuilder();

        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("name.pinyin", keyword);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("status", "1");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchPhraseQuery);
        boolQueryBuilder.must(termQueryBuilder);

        builder.query(boolQueryBuilder);
        Page<TagSearchRes> page = esClient.search("jgxq_tag", builder, TagSearchRes.class, pageNum, pageSize);
        return page;
    }

    private List<TagSearchRes> searchTagByEs(String keyword) {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("name.pinyin", keyword);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("status", "1");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchPhraseQuery);
        boolQueryBuilder.must(termQueryBuilder);

        builder.query(boolQueryBuilder);
        List<TagSearchRes> resList = esClient.search("jgxq_tag", builder, TagSearchRes.class, 10);
        return resList;
    }

}
