package com.edianzu.mall.trace.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.edianzu.mall.trace.TraceContent;
import com.edianzu.mall.trace.service.TraceContext;
import java.util.Map;

@Activate(group = Constants.PROVIDER)
public class TraceProviderFilter implements Filter {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TraceProviderFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = invocation.getAttachments();
        TraceContext context =  TraceContext.getInstance();
        TraceContent content = null;
        if (!attachments.containsKey(TraceContent.TRACE_ID)) {
            context.init();
            content = TraceContext.get();
        } else {
            content = new TraceContent(attachments.get(TraceContent.TRACE_ID));
            content.setRefCnt(attachments.get(TraceContent.STEP));
            context.setTraceContent(content);
        }
        context.setMDC();
        try {
            long start = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            double s = (System.currentTimeMillis() - start)*1.0/1000;
            log.info(TraceContent.LOG_FORMAT, content.getTraceId(), content.getRefCnt(), s);
            return result;
        }finally {
            context.destroy();
            RpcContext.getContext().clearAttachments();
        }
    }
}
