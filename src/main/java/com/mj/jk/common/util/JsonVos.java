package com.mj.jk.common.util;

import com.mj.jk.common.exception.CommonException;
import com.mj.jk.pojo.vo.DataJsonVo;
import com.mj.jk.pojo.vo.JsonVo;
import com.mj.jk.pojo.result.CodeMsg;
import com.mj.jk.pojo.vo.PageJsonVo;
import com.mj.jk.pojo.vo.PageVo;

public class JsonVos {
    public static JsonVo error(String msg) {
        return new JsonVo(false, msg);
    }

    public static JsonVo error(CodeMsg msg) {
        return new JsonVo(msg);
    }

    public static JsonVo error(int code, String msg) {
        return new JsonVo(code, msg);
    }

    public static JsonVo error() {
        return new JsonVo(false);
    }

//    public static JsonVo error(Throwable t) {
//        if (t instanceof CommonException) {
//            CommonException ce = (CommonException) t;
//            return new JsonVo(ce.getCode(), ce.getMessage());
//        } else if (t instanceof BindException) {
//            BindException be = (BindException) t;
//            List<ObjectError> errors = be.getBindingResult().getAllErrors();
//            // 函数式编程的方式：stream
//            List<String> defaultMsgs = errors
//                    .stream().map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.toList());
////            List<String> defaultMsgs = new ArrayList<>(errors.size());
////            for (ObjectError error : errors) {
////                defaultMsgs.add(error.getDefaultMessage());
////            }
//            String msg = StringUtils.collectionToDelimitedString(defaultMsgs, ", ");
//            return error(msg);
//        } else if (t instanceof ConstraintViolationException) {
//            ConstraintViolationException cve = (ConstraintViolationException) t;
//            List<String> msgs = cve.getConstraintViolations()
//                    .stream().map(ConstraintViolation::getMessage)
//                    .collect(Collectors.toList());
//            String msg = StringUtils.collectionToDelimitedString(msgs, ", ");
//            return error(msg);
//        } else { // 其他异常
////            return error(t.getMessage());
//            return error();
//        }
//    }

    public static JsonVo ok(CodeMsg msg) {
        return new JsonVo(msg);
    }

    public static JsonVo ok(String msg) {
        return new JsonVo(true, msg);
    }

    public static <T> DataJsonVo<T> ok(T data) {
        return new DataJsonVo<>(data);
    }

    public static <T> PageJsonVo<T> ok(PageVo<T> pageVo) {
        PageJsonVo<T> pageJsonVo = new PageJsonVo<>();
        pageJsonVo.setCount(pageVo.getCount());
        pageJsonVo.setData(pageVo.getData());
        return pageJsonVo;
    }

    public static JsonVo ok() {
        return new JsonVo();
    }

    public static <T> T raise(String msg) throws CommonException {
        throw new CommonException(msg);
    }

    public static <T> T raise(CodeMsg codeMsg) throws CommonException {
        throw new CommonException(codeMsg);
    }
}
