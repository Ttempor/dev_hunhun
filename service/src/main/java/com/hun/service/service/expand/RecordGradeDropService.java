package com.hun.service.service.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.expand.recordgradedrop.QueryDropGradePageDto;
import com.hun.bean.entity.expand.RecordGradeDrop;
import com.hun.bean.vo.RecordGradeDropVO;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【record_grade_drop】的数据库操作Service
* @createDate 2023-04-05 15:51:29
*/
public interface RecordGradeDropService  {

    /**
     * 退课记录的条件分页
     */
    Page<RecordGradeDropVO> getDropPage(QueryDropGradePageDto queryDropGradePageDto);

    /**
     * 保存退课记录
     */
    void save(RecordGradeDrop recordGradeDrop);

    /**
     * 通过班级id获得某班级在某时间内的退课人数
     * @param gradeId 班级id
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 人数
     */
    Integer getCountByGradeIdAndMonth(long gradeId, long startTimestamp, long endTimestamp);

    /**
     * 获得时间内退课, 通过gradeId进行分组去重
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 分组
     */
    List<RecordGradeDrop> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp);
}
