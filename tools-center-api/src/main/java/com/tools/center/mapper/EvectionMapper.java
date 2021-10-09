//package com.tools.center.mapper;
//
//import com.tools.center.entity.BeAwayApplyMapper;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//
//@Mapper
//public interface EvectionMapper {
//    @Select("select * from tb_evection where userid = #{userId}")
//    List<BeAwayApplyMapper> selectAll(Long userId);
//
//    @Update("update tb_evection set state=1 where id=#{id}")
//    int startTask(Long id);
//
//    @Update("update tb_evection set state=2 where id=#{id}")
//    int endTask(Long id);
//
//    @Select("select * from tb_evection where id=#{id}")
//    BeAwayApplyMapper selectOne(Long id);
//
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    @Insert("insert into tb_evection (userid,evectionname,num,begindate,enddate,destination,reson,state) values (#{userid},#{evectionName},#{num},#{beginDate},#{endDate},#{destination},#{reson},#{state})")
//    int save(BeAwayApplyMapper evection);
//}
