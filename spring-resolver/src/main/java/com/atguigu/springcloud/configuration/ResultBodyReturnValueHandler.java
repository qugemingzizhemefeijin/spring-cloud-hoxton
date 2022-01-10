package com.atguigu.springcloud.configuration;

import com.atguigu.springcloud.domain.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class ResultBodyReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ResultBodyReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class) ||
                AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResultBody.class) ||
                returnType.hasMethodAnnotation(ResultBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        //setRequestHandled(true)表示此函数可以处理请求，不必交给别的代码处理
        // mavContainer.setRequestHandled(true);

        /*webRequest
                .getNativeResponse(HttpServletResponse.class)
                .getWriter()
                .write(JSONObject.toJSONString(returnValue));
         */

        mavContainer.setRequestHandled(true);

        // HttpServletResponse httpResponse = (HttpServletResponse) webRequest.getNativeResponse();

        if(AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResultBody.class) || returnType.hasMethodAnnotation(ResultBody.class)){
            ActionResult<Object> result = new ActionResult<>();
            result.setCode(1);
            result.setMessage("success");
            result.setData(returnValue);

            delegate.handleReturnValue(result, returnType, mavContainer, webRequest);
        } else {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }

        // super.handleReturnValue(result, returnType, mavContainer, webRequest);
    }

}
