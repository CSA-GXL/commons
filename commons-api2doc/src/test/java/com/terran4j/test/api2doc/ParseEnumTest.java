package com.terran4j.test.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.terran4j.commons.api2doc.domain.ApiResultObject;
import com.terran4j.commons.api2doc.other.utils.KeyedList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class ParseEnumTest {

    private static final Logger log = LoggerFactory.getLogger(ParseEnumTest.class);

    public enum MyState {

        @ApiComment("打开")
        open,

        @ApiComment("关闭")
        close;
    }

    public final MyState getState() {
        return MyState.open;
    }

    @Test
    public void testGetEnumComment() throws Throwable {
        String comment = ApiResultObject.getEnumComment(MyState.class);
        Assert.assertEquals("可选值为：\nopen: 打开; \nclose: 关闭;", comment.trim());
    }

    @Test
    public void testParseResultTypeWithEnum() throws Exception {
        log.info("testParseResultTypeWithEnum");
        Method method = ReflectionUtils.findMethod(getClass(), "getState");
        Assert.assertNotNull(method);

        KeyedList<String, ApiResultObject> list = new KeyedList<>();
        ApiResultObject result = ApiResultObject.parseResultType(method, list);
        Assert.assertNotNull(result);
        Assert.assertEquals("可选值为：\nopen: 打开; \nclose: 关闭;",
                result.getComment().getValue().trim());
    }

}
