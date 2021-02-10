package com.jgxq.forum;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.res.PlayerTeamRes;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.controller.PlayerController;
import com.jgxq.front.controller.TeamController;
import com.jgxq.front.define.MatchPosition;
import com.jgxq.front.define.Position;
import com.jgxq.front.define.VerificationCodeType;
import com.jgxq.front.entity.Player;
import com.jgxq.front.mapper.PlayerMapper;
import com.jgxq.front.sender.JGMailSender;
import com.jgxq.front.service.impl.MessageServiceImpl;
import com.jgxq.front.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
class AppTests {

    @Autowired
    private JGMailSender mailSender;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    MessageServiceImpl messageService;

    @Test
    void MessageTest(){
        HashSet<String> set = new HashSet<>();
        System.out.println(set.add("111"));
        System.out.println(set.remove("111"));
        System.out.println(set.remove("111"));
    }

    @Test
    void teamStringTest(){
        QueryWrapper<Player> playerQuery = new QueryWrapper<Player>();
        playerQuery.eq("team",1);
        List<Player> players = playerMapper.selectList(playerQuery);
        System.out.println("------------");
        for (Player player : players) {
            System.out.println(player.getName()+": [jgxq]"+player.getId());
        }
    }

    @Test
    void addMatchPlayer(){
        String source =
                "[{\"name\":\"顾添承\",\"number\":\"2\"},{\"name\":\"杨华钦\",\"number\":\"3\"},{\"name\":\"周创\",\"number\":\"5\"},{\"name\":\"徐翔\",\"number\":\"9\"},{\"name\":\"白云飞\",\"number\":\"12\"},{\"name\":\"杨海峰\",\"number\":\"17\"},{\"name\":\"谭渝川\",\"number\":\"18\"},{\"name\":\"吴浩玮\",\"number\":\"20\"},{\"name\":\"龚锐\",\"number\":\"21\"},{\"name\":\"杨卓凡\",\"number\":\"22\"},{\"name\":\"曾柯\",\"number\":\"23\"},{\"name\":\"夏天\",\"number\":\"1\"},{\"name\":\"伊布热依木·约麦尔\",\"number\":\"19\"},{\"name\":\"胡安勇\",\"number\":\"35\"}]";
        List<Player> players = JSON.parseArray(source, Player.class);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setEnName("");
            players.get(i).setInfos("{\"normal\": [{\"name\": \"身价\", \"value\": \"60万欧元\"}]}");
            players.get(i).setTeam(27);
            players.get(i).setPosition((byte) Position.AM.getValue());
            players.get(i).setWeight(0);
            players.get(i).setHeight(0);
            players.get(i).setStrongFoot((byte)0);
        }
        playerService.saveBatch(players);

    }

    @Test
    void MailTest() throws MessagingException {
        mailSender.sendVerificationCode("1346683197@qq.com","236563", VerificationCodeType.FIND);
    }

    @Test
    void Mails() {
        String str = "675566617\n" +
                "674338325\n" +
                "523140097\n" +
                "1092260680\n" +
                "834673164\n" +
                "1457914017\n" +
                "1471350618\n" +
                "1223524131\n" +
                "1805279152\n" +
                "253735421\n" +
                "306195115\n" +
                "355235144\n" +
                "360844758\n" +
                "370184700\n" +
                "512985261\n" +
                "623222477\n" +
                "755296813\n" +
                "1170092686\n" +
                "915825150\n" +
                "437146476\n" +
                "970079324\n" +
                "794871138\n" +
                "1210046158\n" +
                "331927221\n" +
                "1031660532\n" +
                "782108019\n" +
                "869941616\n" +
                "1059328127\n" +
                "492016596\n" +
                "240412546\n" +
                "756053993\n" +
                "645972224\n" +
                "1183716119\n" +
                "820130946\n" +
                "2847166863\n" +
                "1251086707\n" +
                "542929989\n" +
                "642256035\n" +
                "1187290444\n" +
                "674830015\n" +
                "704350832\n" +
                "414049622\n" +
                "996480626\n" +
                "1272176717\n" +
                "719945889\n" +
                "1298361408\n" +
                "2896506680\n" +
                "1032739073\n" +
                "781852469\n" +
                "1243568601\n" +
                "1141039688\n" +
                "505936919\n" +
                "1027323174\n" +
                "420597314\n" +
                "595724119\n" +
                "785287004\n" +
                "694244219\n" +
                "596322545\n" +
                "1030082240\n" +
                "1141498415\n" +
                "2223978088\n" +
                "2541459571\n" +
                "1808240575\n" +
                "809312031\n" +
                "173106969\n" +
                "583808151\n" +
                "897087475\n" +
                "1120595953\n" +
                "2451999782\n" +
                "1930319386\n" +
                "1346683197\n" +
                "260946760\n" +
                "1026957234\n" +
                "1325900561\n" +
                "1446477197\n" +
                "627487961\n" +
                "764477377\n" +
                "1053017260\n" +
                "1981768068\n" +
                "914867023\n" +
                "1159187077\n" +
                "2992894354\n" +
                "706774561\n" +
                "1823099025\n" +
                "1362335901\n" +
                "1132973907\n" +
                "837908219\n" +
                "909038614\n" +
                "1283474476\n" +
                "1450357640\n" +
                "958555356\n" +
                "1245789904\n" +
                "2643675979\n" +
                "848785578\n" +
                "244110904\n" +
                "280134604\n" +
                "203359074\n" +
                "1441647855\n" +
                "1763757787\n" +
                "2781028775\n" +
                "1012753115\n" +
                "2725480076\n" +
                "709037982\n" +
                "2245433359\n" +
                "2809633022\n" +
                "2602932412\n" +
                "2570603950\n" +
                "952631197\n" +
                "1246772955\n" +
                "2236614786\n" +
                "695038219\n" +
                "2731242046\n" +
                "2679463827\n" +
                "919066044\n" +
                "1115221608\n" +
                "1929945517\n";
        String[] emails = str.split("\n");
        for (String email : emails) {

//            try {
//                mailSender.sendEmail(email+"@qq.com",
//                        "重庆邮电大学经济管理/现代邮政足球队赛前通报",
//                        "重庆邮电大学经济管理/现代邮政足球队将与国际学院进行友谊赛，赛前通报如下：\n" +
//                                "时间：2020年12月08日12:00\n" +
//                                "地点：重庆邮电大学太极足球场\n" +
//                                "集合时间：11:30\n" +
//                                "首发阵容：暂无\n" +
//                                "带队队长：暂无\n" +
//                                "备注：天气冷，注意保暖");
//            } catch (MessagingException e) {
//                System.out.println(email+"@qq.com" + "发送失败");
//            }
        }
    }
    @Test
    void contextLoads() {
    }

}
