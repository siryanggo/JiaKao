package com.mj.jk.common.enhance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mj.jk.common.util.Streams;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.req.page.PageReqVo;

import java.util.List;
import java.util.function.Function;

public class MpPage<T> extends Page<T> {
    private final PageReqVo reqVo;

    public MpPage(PageReqVo reqVo) {
        super(reqVo.getPage(), reqVo.getSize());
        this.reqVo = reqVo;
    }

    private <N> PageVo<N> commonBuldVo(List<N> data) {
        reqVo.setPage(getCurrent());
        reqVo.setSize(getSize());

        PageVo<N> pageVo = new PageVo<>();
        pageVo.setCount(getTotal());
        pageVo.setPages(getPages());
        pageVo.setData(data);
        return pageVo;
    }

    public PageVo<T> buildVo() {
        return commonBuldVo(getRecords());
    }

    public <R> PageVo<R> buildVo(Function<T, R> function) {
        return commonBuldVo(Streams.map(getRecords(), function));
    }
}
