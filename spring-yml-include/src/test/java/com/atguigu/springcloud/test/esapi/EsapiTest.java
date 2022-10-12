package com.atguigu.springcloud.test.esapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.errors.ValidationException;

// 使用此API时，需要引入三个资源文件,分别是ESAPI.properties 、validation.properties、antisamy-esapi.xml
// https://github.com/ESAPI/esapi-java-legacy/releases/tag/esapi-2.5.0.0

// 可以选择新建三个配置文件ESAPI.properties、logback.xml、validation.properties。为什么要新建？新建的配置文件是空的，ESAPI运行时就会安装默认配置进行。
@Slf4j
public class EsapiTest {

    private static final MySQLCodec MYSQL_CODEC = new MySQLCodec(MySQLCodec.Mode.STANDARD);

    // 当来自外部来源（如用户输入或其他不受信任的来源）的数据直接写入日志时，可能会发生日志伪造。
    @Test
    public void encodeForHTMLTest() {
        String text = "<a href=\"https://www.baidu.com\">Hello, esapi</a><script>var a=1;alert(a)</script>";
        String safe = ESAPI.encoder().encodeForHTML(text);
        log.info(safe);
    }

    @Test
    public void getValidSafeHTMLTest() {
        String input = "<p>test <b>this</b> <script>alert(document.cookie)</script><i>right</i> now</p>";
        try {
            String safe2 = ESAPI.validator().getValidSafeHTML("get safe html", input, Integer.MAX_VALUE, true);
            log.info("getValidSafeHTML:{}", safe2);
        } catch (ValidationException e) {
            log.error("error", e);
        }
    }

    @Test
    public void getValidSafeHTMLTest2() {
        String input = "<p>test <b>this</b><i>right</i> now</p>";
        try {
            String safe2 = ESAPI.validator().getValidSafeHTML("get safe html", input, Integer.MAX_VALUE, true);
            log.info("getValidSafeHTML:{}", safe2);
        } catch (ValidationException e) {
            log.error("error", e);
        }
    }

    @Test
    public void encodeForSQLTest() {
        String userName = "foo\" and 1 = 2";
        String password = "foo\" and 1 = 2";

        String query = "SELECT user_id FROM user_data WHERE user_name = '" +
                ESAPI.encoder().encodeForSQL(MYSQL_CODEC, userName) +
                "' and user_password = '"
                + ESAPI.encoder().encodeForSQL(MYSQL_CODEC, password) +
                "'";

        log.info(query);
    }

}
