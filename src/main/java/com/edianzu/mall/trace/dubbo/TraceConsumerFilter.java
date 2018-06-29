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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Activate(group = Constants.CONSUMER)
public class TraceConsumerFilter implements Filter {

    Logger log = LoggerFactory.getLogger(TraceConsumerFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceContext context =  TraceContext.getInstance();
        TraceContent content = TraceContext.get();
        if (content == null) {
            context.init();
            content = TraceContext.get();
        }
        RpcContext.getContext().setAttachment(TraceContent.TRACE_ID, content.getTraceId());
        RpcContext.getContext().setAttachment(TraceContent.STEP, content.nextStep());
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        double s = (System.currentTimeMillis() - start)*1.0/1000;
        log.info(TraceContent.LOG_FORMAT, content.getTraceId(), content.getRefCnt(), s);
        // 调用链增加
        content.currentNextStep();
        context.setMDC();
        return result;
    }
}
