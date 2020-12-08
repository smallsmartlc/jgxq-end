package com.jgxq.front.sender;

import com.jgxq.front.define.VerificationCodeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
@Component
public class JGMailSender {
    @Value("${spring.mail.username}")
    //使用@Value注入application.properties中指定的用户名
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String to, String verificationCode, VerificationCodeTypeEnum type) throws MessagingException {
        String content = null;
        String subject = null;
        if (type == VerificationCodeTypeEnum.REG) {
            content = "<h1>经管雄起</h1>\n" +
                    "<div style = 'color : red'>您正在注册经管雄起官方账号,验证码" + verificationCode + "</div>\n";
            subject = "经管雄起-注册账号";
        } else if(type == VerificationCodeTypeEnum.LOG){
            content = "<h1>经管雄起</h1>\n" +
                    "<div style = 'color : red'>您正在使用验证码登陆经管雄起官方账号,验证码" + verificationCode + "</div>\n";
            subject = "经管雄起-身份验证";
        }else if(type == VerificationCodeTypeEnum.FIND){
            content = "<h1>经管雄起</h1>\n" +
                    "<div style = 'color : red'>您正在使用验证码找回经管雄起官方账号密码,验证码" + verificationCode + "</div>\n";
            subject = "经管雄起-找回密码";
        }
        sendEmail(to, subject,
                content);
    }

    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);//true代表支持html
        mailSender.send(message);
    }

}
