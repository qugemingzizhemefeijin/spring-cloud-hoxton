package com.atguigu.springcloud.other.document.commonmark;

import com.atguigu.springcloud.other.document.wkhtml.HtmlToPdfUtils;
import org.apache.commons.lang.StringUtils;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyPDF {

    public static void main(String[] args) throws IOException {
        String[] files = new String[]{"0055Linux Awk命令详解之一参数和变量", "0056Linux Awk命令详解之二模式", "0057Linux Awk命令详解之三内置函数", "0058Linux Awk命令详解之四数组", "0059Linux Awk命令详解之五自定义函数", "0060Linux Awk命令详解之六PROCINFO内置变量", "0068Linux Awk之八拾遗篇-正则表达式", "0069Linux Awk之九拾遗篇-转义字符、排序、getline等", "0070Linux Awk之十扩展csv、webargs、encodargs", "0071Linux Awk之十一gawkextlib扩展", "0061Linux Awk命令详解之七终章高级使用案例与坑"};

        List<String> allContent = new ArrayList<>(files.length);

        for (String f : files) {
            String content = new String(Files.readAllBytes(Paths.get("E:\\资料\\" + f + ".txt")), StandardCharsets.UTF_8);

            System.out.println(content);
            allContent.add(content + "\n");
        }

        String destFileName = "E:\\file\\output.pdf";

        File file = new File(destFileName);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        String htmlHeader = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<title>Markdown to HTML</title>\n" +
                "<style>\n" +
                "/*! normalize.css v3.0.1 | MIT License | git.io/normalize */\n" +
                "\n" +
                "/**\n" +
                " * 1. Set default font family to sans-serif.\n" +
                " * 2. Prevent iOS text size adjust after orientation change, without disabling\n" +
                " *    user zoom.\n" +
                " */\n" +
                "\n" +
                "html {\n" +
                "  font-family: sans-serif; /* 1 */\n" +
                "  -ms-text-size-adjust: 100%; /* 2 */\n" +
                "  -webkit-text-size-adjust: 100%; /* 2 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Remove default margin.\n" +
                " */\n" +
                "\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                "/* HTML5 display definitions\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Correct `block` display not defined for any HTML5 element in IE 8/9.\n" +
                " * Correct `block` display not defined for `details` or `summary` in IE 10/11 and Firefox.\n" +
                " * Correct `block` display not defined for `main` in IE 11.\n" +
                " */\n" +
                "\n" +
                "article,\n" +
                "aside,\n" +
                "details,\n" +
                "figcaption,\n" +
                "figure,\n" +
                "footer,\n" +
                "header,\n" +
                "hgroup,\n" +
                "main,\n" +
                "nav,\n" +
                "section,\n" +
                "summary {\n" +
                "  display: block;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 1. Correct `inline-block` display not defined in IE 8/9.\n" +
                " * 2. Normalize vertical alignment of `progress` in Chrome, Firefox, and Opera.\n" +
                " */\n" +
                "\n" +
                "audio,\n" +
                "canvas,\n" +
                "progress,\n" +
                "video {\n" +
                "  display: inline-block; /* 1 */\n" +
                "  vertical-align: baseline; /* 2 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Prevent modern browsers from displaying `audio` without controls.\n" +
                " * Remove excess height in iOS 5 devices.\n" +
                " */\n" +
                "\n" +
                "audio:not([controls]) {\n" +
                "  display: none;\n" +
                "  height: 0;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address `[hidden]` styling not present in IE 8/9/10.\n" +
                " * Hide the `template` element in IE 8/9/11, Safari, and Firefox < 22.\n" +
                " */\n" +
                "\n" +
                "[hidden],\n" +
                "template {\n" +
                "  display: none;\n" +
                "}\n" +
                "\n" +
                "/* Links\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Remove the gray background color from active links in IE 10.\n" +
                " */\n" +
                "\n" +
                "a {\n" +
                "  background: transparent;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Improve readability when focused and also mouse hovered in all browsers.\n" +
                " */\n" +
                "\n" +
                "a:active,\n" +
                "a:hover {\n" +
                "  outline: 0;\n" +
                "}\n" +
                "\n" +
                "/* Text-level semantics\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Address styling not present in IE 8/9/10/11, Safari, and Chrome.\n" +
                " */\n" +
                "\n" +
                "abbr[title] {\n" +
                "  border-bottom: 1px dotted;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address style set to `bolder` in Firefox 4+, Safari, and Chrome.\n" +
                " */\n" +
                "\n" +
                "b,\n" +
                "strong {\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address styling not present in Safari and Chrome.\n" +
                " */\n" +
                "\n" +
                "dfn {\n" +
                "  font-style: italic;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address variable `h1` font-size and margin within `section` and `article`\n" +
                " * contexts in Firefox 4+, Safari, and Chrome.\n" +
                " */\n" +
                "\n" +
                "h1 {\n" +
                "  font-size: 2em;\n" +
                "  margin: 0.67em 0;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address styling not present in IE 8/9.\n" +
                " */\n" +
                "\n" +
                "mark {\n" +
                "  background: #ff0;\n" +
                "  color: #000;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address inconsistent and variable font size in all browsers.\n" +
                " */\n" +
                "\n" +
                "small {\n" +
                "  font-size: 80%;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Prevent `sub` and `sup` affecting `line-height` in all browsers.\n" +
                " */\n" +
                "\n" +
                "sub,\n" +
                "sup {\n" +
                "  font-size: 75%;\n" +
                "  line-height: 0;\n" +
                "  position: relative;\n" +
                "  vertical-align: baseline;\n" +
                "}\n" +
                "\n" +
                "sup {\n" +
                "  top: -0.5em;\n" +
                "}\n" +
                "\n" +
                "sub {\n" +
                "  bottom: -0.25em;\n" +
                "}\n" +
                "\n" +
                "/* Embedded content\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Remove border when inside `a` element in IE 8/9/10.\n" +
                " */\n" +
                "\n" +
                "img {\n" +
                "  border: 0;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Correct overflow not hidden in IE 9/10/11.\n" +
                " */\n" +
                "\n" +
                "svg:not(:root) {\n" +
                "  overflow: hidden;\n" +
                "}\n" +
                "\n" +
                "/* Grouping content\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Address margin not present in IE 8/9 and Safari.\n" +
                " */\n" +
                "\n" +
                "figure {\n" +
                "  margin: 1em 40px;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address differences between Firefox and other browsers.\n" +
                " */\n" +
                "\n" +
                "hr {\n" +
                "  -moz-box-sizing: content-box;\n" +
                "  box-sizing: content-box;\n" +
                "  height: 0;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Contain overflow in all browsers.\n" +
                " */\n" +
                "\n" +
                "pre {\n" +
                "  overflow: auto;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address odd `em`-unit font size rendering in all browsers.\n" +
                " */\n" +
                "\n" +
                "code,\n" +
                "kbd,\n" +
                "pre,\n" +
                "samp {\n" +
                "  font-family: monospace, monospace;\n" +
                "  font-size: 1em;\n" +
                "}\n" +
                "\n" +
                "/* Forms\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Known limitation: by default, Chrome and Safari on OS X allow very limited\n" +
                " * styling of `select`, unless a `border` property is set.\n" +
                " */\n" +
                "\n" +
                "/**\n" +
                " * 1. Correct color not being inherited.\n" +
                " *    Known issue: affects color of disabled elements.\n" +
                " * 2. Correct font properties not being inherited.\n" +
                " * 3. Address margins set differently in Firefox 4+, Safari, and Chrome.\n" +
                " */\n" +
                "\n" +
                "button,\n" +
                "input,\n" +
                "optgroup,\n" +
                "select,\n" +
                "textarea {\n" +
                "  color: inherit; /* 1 */\n" +
                "  font: inherit; /* 2 */\n" +
                "  margin: 0; /* 3 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address `overflow` set to `hidden` in IE 8/9/10/11.\n" +
                " */\n" +
                "\n" +
                "button {\n" +
                "  overflow: visible;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address inconsistent `text-transform` inheritance for `button` and `select`.\n" +
                " * All other form control elements do not inherit `text-transform` values.\n" +
                " * Correct `button` style inheritance in Firefox, IE 8/9/10/11, and Opera.\n" +
                " * Correct `select` style inheritance in Firefox.\n" +
                " */\n" +
                "\n" +
                "button,\n" +
                "select {\n" +
                "  text-transform: none;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 1. Avoid the WebKit bug in Android 4.0.* where (2) destroys native `audio`\n" +
                " *    and `video` controls.\n" +
                " * 2. Correct inability to style clickable `input` types in iOS.\n" +
                " * 3. Improve usability and consistency of cursor style between image-type\n" +
                " *    `input` and others.\n" +
                " */\n" +
                "\n" +
                "button,\n" +
                "html input[type=\"button\"], /* 1 */\n" +
                "input[type=\"reset\"],\n" +
                "input[type=\"submit\"] {\n" +
                "  -webkit-appearance: button; /* 2 */\n" +
                "  cursor: pointer; /* 3 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Re-set default cursor for disabled elements.\n" +
                " */\n" +
                "\n" +
                "button[disabled],\n" +
                "html input[disabled] {\n" +
                "  cursor: default;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Remove inner padding and border in Firefox 4+.\n" +
                " */\n" +
                "\n" +
                "button::-moz-focus-inner,\n" +
                "input::-moz-focus-inner {\n" +
                "  border: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Address Firefox 4+ setting `line-height` on `input` using `!important` in\n" +
                " * the UA stylesheet.\n" +
                " */\n" +
                "\n" +
                "input {\n" +
                "  line-height: normal;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * It's recommended that you don't attempt to style these elements.\n" +
                " * Firefox's implementation doesn't respect box-sizing, padding, or width.\n" +
                " *\n" +
                " * 1. Address box sizing set to `content-box` in IE 8/9/10.\n" +
                " * 2. Remove excess padding in IE 8/9/10.\n" +
                " */\n" +
                "\n" +
                "input[type=\"checkbox\"],\n" +
                "input[type=\"radio\"] {\n" +
                "  box-sizing: border-box; /* 1 */\n" +
                "  padding: 0; /* 2 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Fix the cursor style for Chrome's increment/decrement buttons. For certain\n" +
                " * `font-size` values of the `input`, it causes the cursor style of the\n" +
                " * decrement button to change from `default` to `text`.\n" +
                " */\n" +
                "\n" +
                "input[type=\"number\"]::-webkit-inner-spin-button,\n" +
                "input[type=\"number\"]::-webkit-outer-spin-button {\n" +
                "  height: auto;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 1. Address `appearance` set to `searchfield` in Safari and Chrome.\n" +
                " * 2. Address `box-sizing` set to `border-box` in Safari and Chrome\n" +
                " *    (include `-moz` to future-proof).\n" +
                " */\n" +
                "\n" +
                "input[type=\"search\"] {\n" +
                "  -webkit-appearance: textfield; /* 1 */\n" +
                "  -moz-box-sizing: content-box;\n" +
                "  -webkit-box-sizing: content-box; /* 2 */\n" +
                "  box-sizing: content-box;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Remove inner padding and search cancel button in Safari and Chrome on OS X.\n" +
                " * Safari (but not Chrome) clips the cancel button when the search input has\n" +
                " * padding (and `textfield` appearance).\n" +
                " */\n" +
                "\n" +
                "input[type=\"search\"]::-webkit-search-cancel-button,\n" +
                "input[type=\"search\"]::-webkit-search-decoration {\n" +
                "  -webkit-appearance: none;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Define consistent border, margin, and padding.\n" +
                " */\n" +
                "\n" +
                "fieldset {\n" +
                "  border: 1px solid #c0c0c0;\n" +
                "  margin: 0 2px;\n" +
                "  padding: 0.35em 0.625em 0.75em;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 1. Correct `color` not being inherited in IE 8/9/10/11.\n" +
                " * 2. Remove padding so people aren't caught out if they zero out fieldsets.\n" +
                " */\n" +
                "\n" +
                "legend {\n" +
                "  border: 0; /* 1 */\n" +
                "  padding: 0; /* 2 */\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Remove default vertical scrollbar in IE 8/9/10/11.\n" +
                " */\n" +
                "\n" +
                "textarea {\n" +
                "  overflow: auto;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * Don't inherit the `font-weight` (applied by a rule above).\n" +
                " * NOTE: the default cannot safely be changed in Chrome and Safari on OS X.\n" +
                " */\n" +
                "\n" +
                "optgroup {\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "/* Tables\n" +
                "   ========================================================================== */\n" +
                "\n" +
                "/**\n" +
                " * Remove most spacing between table cells.\n" +
                " */\n" +
                "\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "  border-spacing: 0;\n" +
                "}\n" +
                "\n" +
                "td,\n" +
                "th {\n" +
                "  padding: 0;\n" +
                "}\n" +
                "/* General Setting */\n" +
                "\n" +
                "/* Small screens (default) */\n" +
                "html { font-size: 100%; }\n" +
                "\n" +
                "/* Medium screens (640px) */\n" +
                "@media (min-width: 40rem) {\n" +
                "  html { font-size: 112%; }\n" +
                "}\n" +
                "\n" +
                "/* Large screens (1024px) */\n" +
                "@media (min-width: 64rem) {\n" +
                "  html { font-size: 120%; }\n" +
                "}\n" +
                "\n" +
                "/* Grid */\n" +
                "\n" +
                "/* Ditto */\n" +
                "\n" +
                "body {\n" +
                "    color: #333;\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "\n" +
                "    font-family: Verdana, Arial;\n" +
                "    font-size: 0.8rem;\n" +
                "}\n" +
                "\n" +
                "#sidebar {\n" +
                "    margin-top: 0;\n" +
                "    padding-left: 25px;\n" +
                "\tpadding-bottom:25px;\n" +
                "    padding-top: 25px;\n" +
                "\n" +
                "    box-shadow: 0 0 40px #CCC;\n" +
                "    -webkit-box-shadow: 0 0 40px #CCC;\n" +
                "    -moz-box-shadow: 0 0 40px #CCC;\n" +
                "    border-right: 1px solid #BBB;\n" +
                "}\n" +
                "\n" +
                "@media (min-width: 40rem) {\n" +
                "\t#sidebar {\n" +
                "\twidth: 280px;\n" +
                "    position: fixed;\n" +
                "    height: 100%;\n" +
                "    margin-right: 20px;\n" +
                "\tpadding-bottom:0px;\n" +
                "    padding-top: 0px;\n" +
                "    overflow-y: scroll;\n" +
                "    overflow: -moz-scrollbars-vertical;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "#sidebar h1 {\n" +
                "    font-size: 25px;\n" +
                "    margin-bottom: 0px;\n" +
                "    padding-bottom: 0px;\n" +
                "}\n" +
                "\n" +
                "#sidebar h1 a:link, #sidebar h1 a:visited {\n" +
                "    color: #333;\n" +
                "}\n" +
                "\n" +
                "#sidebar h2 {\n" +
                "    font-size: 0.7rem;\n" +
                "}\n" +
                "\n" +
                "#sidebar h5 {\n" +
                "    margin-top: 20px;\n" +
                "    margin-bottom: 0;\n" +
                "}\n" +
                "\n" +
                "#sidebar a:visited, #sidebar a:link {\n" +
                "    color: #4682BE;\n" +
                "    text-decoration: none;\n" +
                "}\n" +
                "\n" +
                "#sidebar ul {\n" +
                "    list-style-type: none;\n" +
                "    margin: 0;\n" +
                "    padding-left: 10px;\n" +
                "    padding-top: 0;\n" +
                "}\n" +
                "\n" +
                "#sidebar ol {\n" +
                "    margin: 0;\n" +
                "    padding-left: 30px;\n" +
                "    padding-top: 0;\n" +
                "}\n" +
                "\n" +
                "#sidebar ul li:before {  /* a hack to have dashes as a list style */\n" +
                "    content: \"-\";\n" +
                "    position: relative;\n" +
                "    left: -5px;\n" +
                "}\n" +
                "\n" +
                "#sidebar ul li,\n" +
                "#sidebar ol li {\n" +
                "    margin-top: 0;\n" +
                "    margin-bottom: 0.2rem;\n" +
                "    margin-left: 10px;\n" +
                "    padding: 0;\n" +
                "\n" +
                "    text-indent: -5px;  /* to compensate for the padding for the dash */\n" +
                "    font-size: 0.7rem;\n" +
                "}\n" +
                "\n" +
                "form.searchBox {\n" +
                "  width: 180px;\n" +
                "  border: 1px solid #4682BE;\n" +
                "  height:20px;\n" +
                "  position: relative;\n" +
                "}\n" +
                "\n" +
                "input[type=search] {\n" +
                "  position: absolute;\n" +
                "  top: 0;\n" +
                "  left: 0;\n" +
                "  width: 160px;\n" +
                "  height: 18px;\n" +
                "  text-align: left;\n" +
                "  border: none;\n" +
                "  outline: none;\n" +
                "}\n" +
                "\n" +
                "input.searchButton {\n" +
                "  position: absolute;\n" +
                "  top: 0;\n" +
                "  left: 160px;\n" +
                "  height: 18px;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "pre{\n" +
                "    margin-left: auto;\n" +
                "    margin-right: auto;\n" +
                "    padding-top: 10px;\n" +
                "    padding-bottom: 10px;\n" +
                "    padding-left: 0.7rem;\n" +
                "\tline-height: 1.2;\n" +
                "\n" +
                "    color: #FFF;\n" +
                "\n" +
                "    background: #111;\n" +
                "    border-radius: 5px;\n" +
                "}\n" +
                "\n" +
                "code {\n" +
                "    color: #a6e22e;\n" +
                "    font-size: 0.7rem;\n" +
                "    font-weight: normal;\n" +
                "    font-family: Consolas,\"Courier New\",Courier,FreeMono,monospace;\n" +
                "\n" +
                "    background: #111;\n" +
                "    border-radius: 2px;\n" +
                "}\n" +
                "\n" +
                "p code,\n" +
                "li>code,\n" +
                "h2>code,\n" +
                "h3>code{\n" +
                "  padding-left: 3px;\n" +
                "  padding-right: 3px;\n" +
                "  color: #c7254e;\n" +
                "  background: #f9f2f4;\n" +
                "}\n" +
                "\n" +
                "h2 {\n" +
                "    margin-top: 50px;\n" +
                "    margin-bottom: 0px;\n" +
                "\n" +
                "    padding-top: 20px;\n" +
                "    padding-bottom: 0px;\n" +
                "\n" +
                "    font-size: 18px;\n" +
                "    text-align: left;\n" +
                "\n" +
                "    border-top: 2px solid #666;\n" +
                "\n" +
                "\tcounter-increment: section;\n" +
                "}\n" +
                "\n" +
                "h2:before {\n" +
                "\tcontent: counter(section) \". \";\n" +
                "}\n" +
                "\n" +
                "h3 {\n" +
                "    margin-top: 50px;\n" +
                "    margin-bottom: 0px;\n" +
                "\n" +
                "    padding-top: 20px;\n" +
                "    padding-bottom: 0px;\n" +
                "\n" +
                "    text-align: left;\n" +
                "    border-top: 1px dotted #777;\n" +
                "}\n" +
                "\n" +
                "h2:hover,\n" +
                "h3:hover {\n" +
                "  color: #ED1C24;\n" +
                "}\n" +
                "\n" +
                "img {\n" +
                "    max-width: 90%;\n" +
                "    display: block;\n" +
                "\n" +
                "    margin-left: auto;\n" +
                "    margin-right: auto;\n" +
                "    margin-top: 40px;\n" +
                "    margin-bottom: 40px;\n" +
                "\n" +
                "    border-radius: 5px;\n" +
                "}\n" +
                "\n" +
                "ul {\n" +
                "    display: block;\n" +
                "    list-style-type: none;\n" +
                "\tpadding-top: 0.5rem;\n" +
                "\tpadding-bottom:0.5rem;\n" +
                "}\n" +
                "\n" +
                "ol {\n" +
                "    display: block;\n" +
                "\tpadding-top: 0.5rem;\n" +
                "\tpadding-bottom:0.5rem;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "ul li:before {  /* a hack to have dashes as a list style */\n" +
                "    content: \"-\";\n" +
                "    position: relative;\n" +
                "    left: -5px;\n" +
                "}\n" +
                "\n" +
                "ul li,\n" +
                "ol li{\n" +
                "    text-indent: -5px;  /* to compensate for the padding for the dash */\n" +
                "    font-size: 0.8rem;\n" +
                "}\n" +
                "\n" +
                "ul li.link,\n" +
                "ol li.link {\n" +
                "    color: #2980b9;\n" +
                "    text-decoration: none;\n" +
                "    font-size: 0.7rem;\n" +
                "    font-weight: bold;\n" +
                "    cursor: pointer;\n" +
                "}\n" +
                "\n" +
                "a:link, a:visited {\n" +
                "    color: #4682BE;\n" +
                "    text-decoration: none;\n" +
                "}\n" +
                "\n" +
                ".content-toc{\n" +
                "    background: #bdc3c7;\n" +
                "    border-radius: 5px;\n" +
                "}\n" +
                "\n" +
                "table{display:block;width:100%;overflow:auto;word-break:normal;word-break:keep-all}\n" +
                "\n" +
                "table th{font-weight:bold}\n" +
                "\n" +
                "table th,\n" +
                "table td{padding:6px 13px;border:1px solid #ddd}\n" +
                "\n" +
                "table tr{background-color:#fff;border-top:1px solid #ccc}\n" +
                "table tr:nth-child(2n){background-color:#f8f8f8}\n" +
                "\n" +
                "\n" +
                ".progress-indicator-2 {\n" +
                "    position: fixed;\n" +
                "    top: 0;\n" +
                "    left: 0;\n" +
                "    height: 3px;\n" +
                "    background-color: #0A74DA;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * okaidia theme for JavaScript, CSS and HTML\n" +
                " * Loosely based on Monokai textmate theme by http://www.monokai.nl/\n" +
                " * @author ocodia\n" +
                " */\n" +
                "\n" +
                "code[class*=\"language-\"],\n" +
                "pre[class*=\"language-\"] {\n" +
                "\tcolor: #a6e22e;\n" +
                "\ttext-shadow: 0 1px rgba(0,0,0,0.3);\n" +
                "\tfont-family: Consolas, Monaco, 'Andale Mono', monospace;\n" +
                "\tdirection: ltr;\n" +
                "\ttext-align: left;\n" +
                "\twhite-space: pre;\n" +
                "\tword-spacing: normal;\n" +
                "\tword-break: normal;\n" +
                "\n" +
                "\t-moz-tab-size: 4;\n" +
                "\t-o-tab-size: 4;\n" +
                "\ttab-size: 4;\n" +
                "\n" +
                "\t-webkit-hyphens: none;\n" +
                "\t-moz-hyphens: none;\n" +
                "\t-ms-hyphens: none;\n" +
                "\thyphens: none;\n" +
                "}\n" +
                "\n" +
                "/* Code blocks */\n" +
                "pre[class*=\"language-\"] {\n" +
                "\tpadding: 1em;\n" +
                "\tmargin: .5em 0;\n" +
                "\toverflow: auto;\n" +
                "\tborder-radius: 0.3em;\n" +
                "}\n" +
                "\n" +
                ":not(pre) > code[class*=\"language-\"],\n" +
                "pre[class*=\"language-\"] {\n" +
                "\tbackground: #272822;\n" +
                "}\n" +
                "\n" +
                "/* Inline code */\n" +
                ":not(pre) > code[class*=\"language-\"] {\n" +
                "\tpadding: .1em;\n" +
                "\tborder-radius: .3em;\n" +
                "}\n" +
                "\n" +
                ".token.comment,\n" +
                ".token.prolog,\n" +
                ".token.doctype,\n" +
                ".token.cdata {\n" +
                "\tcolor: #75715e;\n" +
                "}\n" +
                "\n" +
                ".token.punctuation {\n" +
                "\tcolor: #f8f8f2;\n" +
                "}\n" +
                "\n" +
                ".namespace {\n" +
                "\topacity: .7;\n" +
                "}\n" +
                "\n" +
                ".token.property,\n" +
                ".token.tag,\n" +
                ".token.constant,\n" +
                ".token.symbol {\n" +
                "\tcolor: #f92672;\n" +
                "}\n" +
                "\n" +
                ".token.boolean,\n" +
                ".token.number{\n" +
                "\tcolor: #ae81ff;\n" +
                "}\n" +
                "\n" +
                ".token.selector,\n" +
                ".token.attr-name,\n" +
                ".token.string,\n" +
                ".token.builtin {\n" +
                "\tcolor: #a6e22e;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".token.operator,\n" +
                ".token.entity,\n" +
                ".token.url,\n" +
                ".language-css .token.string,\n" +
                ".style .token.string,\n" +
                ".token.variable {\n" +
                "\tcolor: #f92672;\n" +
                "}\n" +
                "\n" +
                ".token.atrule,\n" +
                ".token.attr-value\n" +
                "{\n" +
                "\tcolor: #e6db74;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".token.keyword{\n" +
                "color: #66d9ef;\n" +
                "}\n" +
                "\n" +
                ".token.regex,\n" +
                ".token.important {\n" +
                "\tcolor: #fd971f;\n" +
                "}\n" +
                "\n" +
                ".token.important {\n" +
                "\tfont-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".token.entity {\n" +
                "\tcursor: help;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>";

        String htmlFooter = "</body>\n" +
                "</html>";

        // 解析 Markdown 内容
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(parser.parse(StringUtils.join(allContent, "")));

        System.out.println(htmlContent);

        // 需要配合wkhtmltopdf来将 HTML 到 PDF 转换.
        String htmlPath = HtmlToPdfUtils.stringToHtml(htmlHeader + htmlContent + htmlFooter);

        HtmlToPdfUtils.convert(htmlPath, "awk命令从入门到精通.pdf");

        Files.delete(Paths.get(htmlPath));

        System.out.println("PDF 创建成功！");
    }

}
