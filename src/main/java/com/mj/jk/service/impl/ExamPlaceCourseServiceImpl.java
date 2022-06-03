package com.mj.jk.service.impl;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.jk.common.enhance.MpPage;
import com.mj.jk.common.enhance.MpQueryWrapper;
import com.mj.jk.common.mapStruct.MapStructs;
import com.mj.jk.common.util.JsonVos;
import com.mj.jk.common.util.Uploads;
import com.mj.jk.mapper.ExamPlaceCourseMapper;
import com.mj.jk.pojo.po.ExamPlaceCourse;
import com.mj.jk.pojo.result.CodeMsg;
import com.mj.jk.pojo.vo.PageVo;
import com.mj.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.mj.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.mj.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;
import com.mj.jk.service.ExamPlaceCourseService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ExamPlaceCourseServiceImpl extends ServiceImpl<ExamPlaceCourseMapper, ExamPlaceCourse> implements ExamPlaceCourseService {

    @Override
    @Transactional(readOnly = true)
    public PageVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query) {
        MpQueryWrapper<ExamPlaceCourseVo> wrapper = new MpQueryWrapper<>();
        Integer placeId = query.getPlaceId();
        Integer provinceId = query.getProvinceId();
        Integer cityId = query.getCityId();
        Short type = query.getType();
        // 类型
        if (type != null && type >= 0) {
            wrapper.eq("c.type", type);
        }

        // 考场 -> 城市 -> 省份
        if (placeId != null && placeId > 0) {
            wrapper.eq("c.place_id", placeId);
        } else  if (cityId != null && cityId > 0) {
            wrapper.eq("p.city_id", cityId);
        } else if (provinceId != null && provinceId > 0) {
            wrapper.eq("p.province_id", provinceId);
        }

        // 关键词
        wrapper.like(query.getKeyword(), "c.name", "c.intro");

        return baseMapper
                .selectPageVos(new MpPage<>(query), wrapper)
                .buildVo();

        // SELECT COUNT(*) FROM exam_place_course c WHERE (province_id = ?)
        // SELECT COUNT(*) FROM exam_place_course c WHERE (city_id = ?)

//        MpQueryWrapper<ExamPlaceCourseVo> wrapper = new MpQueryWrapper<>();
//        Integer placeId = query.getPlaceId();
//        Integer provinceId = query.getProvinceId();
//        Integer cityId = query.getCityId();
//        Short type = query.getType();
//        // 类型
//        if (type != null && type >= 0) {
//            wrapper.eq(ExamPlaceCourseVo::getType, type);
//        }
//        // 考场 -> 城市 -> 省份
//        if (placeId != null && placeId > 0) {
//            wrapper.eq(ExamPlaceCourseVo::getPlaceId, placeId);
//        } else  if (cityId != null && cityId > 0) {
//            wrapper.eq(ExamPlaceCourseVo::getCityId, cityId);
//        } else if (provinceId != null && provinceId > 0) {
//            wrapper.eq(ExamPlaceCourseVo::getProvinceId, provinceId);
//        }
//        // 关键词
//        wrapper.like(query.getKeyword(), ExamPlaceCourseVo::getName, ExamPlaceCourseVo::getIntro);
//        return baseMapper
//                .selectPageVos(new MpPage<>(query), wrapper)
//                .buildVo();
        // 通过province_id查询时：Unknown column 'province_id' in 'where clause'
        // 通过province_id查询时：Unknown column 'city_id' in 'where clause'
        // 通过name查询时：Column 'name' in where clause is ambiguous
    }

    @Override
    public boolean saveOrUpdate(ExamPlaceCourseReqVo reqVo) {
        try {
            ExamPlaceCourse po = MapStructs.INSTANCE.reqVo2po(reqVo);

            // 上传图片
            String filepath = Uploads.uploadImage(reqVo.getCoverFile());

            // 有新的图片上传成功
            if (filepath != null) {
                // 设置新的封面
                po.setCover(filepath);
            }

            // 保存po
            boolean ret = saveOrUpdate(po);
            if (ret && filepath != null) {
                // 删除旧封面
                Uploads.deleteFile(reqVo.getCover());
            }
            return ret;
        } catch (Exception e) {
            return JsonVos.raise(CodeMsg.UPLOAD_IMG_ERROR);
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        ExamPlaceCourse course = getById(id);
        try {
            if (super.removeById(id)) {
                Uploads.deleteFile(course.getCover());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    @Override
//    public boolean removeByIds(Collection<? extends Serializable> idList) {
//        if (CollectionUtils.isEmpty(idList)) return false;
//        // 批量查出要删除的对象
//        List<ExamPlaceCourse> courses = listByIds(idList);
//
//        // 挨个删除
//        for (ExamPlaceCourse course : courses) {
//            try {
//                // 如果记录删除成功，才会删除图片
//                if (removeById(course.getId())) {
//                    Uploads.deleteFile(course.getCover());
//                }
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return true;
//
////        // 如果数据库删除失败，就直接返回，不删除图片
////        boolean ret = super.removeByIds(idList);
////        if (!ret) return false;
////
////        for (ExamPlaceCourse course : courses) {
////            try {
////                Uploads.deleteFile(course.getCover());
////            } catch (Exception e) {
////                return false;
////            }
////        }
////        return true;
//    }
}