package com.tools.center.mapper;

import com.tools.center.entity.SiteMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SiteMessageMapper {

    @Select("select * from tb_sitemessage where userid=#{userid}")
    List<SiteMessage> selectMsgList(Long userid);

    @Select("select * from tb_sitemessage where id=#{id} and userid=#{userid}")
    SiteMessage selectOneMsg(@Param("id") Long id, @Param("userid") Long userid);

    @Update("update tb_sitemessage where id=#{id} and userid=#{userid}")
    int updateMsgRead(@Param("id") Long id, @Param("userid") Long userid);

    @Insert("insert into tb_sitemessage (userid,taskid,type,isread) values (#{userid},#{taskid},#{type},#{isread})")
    int insertMsg(@Param("userid") Long userId, @Param("taskid") String taskId, @Param("type") Integer type, @Param("isread") Integer isread);
}
