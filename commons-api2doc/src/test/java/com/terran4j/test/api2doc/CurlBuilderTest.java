package com.terran4j.test.api2doc;

import com.terran4j.commons.api2doc.annotations.Api2Doc;
import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.terran4j.commons.api2doc.domain.ApiDocObject;
import com.terran4j.commons.api2doc.impl.CurlBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.*;

@Api2Doc(id = "curl")
@RequestMapping(value = "/api/v1/curl")
public class CurlBuilderTest extends BaseApi2DocTest {

    @RequestMapping(value = "/getting", method = RequestMethod.GET)
    public void withGet(
            @ApiComment(sample = "键1") String k1,
            @ApiComment(sample = "键2") String k2) {
    }

    @Test
    public void testToCurlWithGet() throws Exception {
        ApiDocObject doc = loadDoc("withGet");
        String curl = CurlBuilder.toCurl(doc, serverURL);
        Assert.assertEquals(
                "curl -X GET \\\n" +
                        " \"http://localhost:8080/api/v1/curl/getting?k1=%E9%94%AE1&k2=%E9%94%AE2\"",
                curl);
    }

    @RequestMapping(value = "/normal/{p1}/abc/{p2}",
            method = RequestMethod.POST)
    public void simple(
            @PathVariable("p1") Long p1,
            @PathVariable("p2") String p2,
            @RequestParam("myParam") String myParam,
            @RequestHeader("myHeader") String myHeader,
            @CookieValue("myCookie") String myCookie) {
    }

    @Test
    public void testToCurlSimple() throws Exception {
        ApiDocObject doc = loadDoc("simple");
        String curl = CurlBuilder.toCurl(doc, serverURL);
        Assert.assertEquals(
                "curl -X POST \\\n" +
                        " -H \"myHeader: myHeader\" \\\n" +
                        " -b \"myCookie=myCookie\" \\\n" +
                        " -d \"myParam=myParam\" \\\n" +
                        " \"http://localhost:8080/api/v1/curl/normal/0/abc/p2\"",
                curl);
    }

    @RequestMapping(value = "/comment/{p1}/{p2}",
            method = RequestMethod.POST)
    public void withComment(
            @ApiComment(value = "路径参数", sample = "123")
            @PathVariable("p1") Long p1,

            @ApiComment(value = "路径参数", sample = "abc")
            @PathVariable("p2") String p2,

            @ApiComment(value = "请求参数", sample = "k123")
            @RequestParam("k1") String k1,

            @ApiComment(value = "Header参数", sample = "false")
            @RequestHeader("k2") boolean k2,

            @ApiComment(value = "Cookie参数", sample = "5.86")
            @CookieValue("k3") double k3) {
    }

    @Test
    public void testToCurlWithComment() throws Exception {
        ApiDocObject doc = loadDoc("withComment");
        String curl = CurlBuilder.toCurl(doc, serverURL);
        Assert.assertEquals(
                "curl -X POST \\\n" +
                        " -H \"k2: false\" \\\n" +
                        " -b \"k3=5.86\" \\\n" +
                        " -d \"k1=k123\" \\\n" +
                        " \"http://localhost:8080/api/v1/curl/comment/123/abc\"",
                curl);
    }

}