//package com.tools.center.service.impl;
//
//import com.tools.center.entity.BeAwayApplyMapper;
//import com.tools.center.mapper.EvectionMapper;
//import com.tools.center.service.IActFlowCustomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class EvectionService implements IActFlowCustomService {
//
//    @Autowired
//    private EvectionMapper evectionMapper;
//
//
//    /**
//     * 查询出差列表
//     * @param userId
//     * @return
//     */
//    public List<BeAwayApplyMapper> findList(Long userId) {
//        return evectionMapper.selectAll(userId);
//    }
//
//    /**
//     * 查询一条出差信息
//     * @param id
//     * @return
//     */
//    public BeAwayApplyMapper findOne(Long id){
//        return evectionMapper.selectOne(id);
//    }
//
//    /**
//     * 添加 出差任务
//     * @param evection
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public int addEvection(BeAwayApplyMapper evection) {
//        int code = evectionMapper.save(evection);
//        return code;
//    }
//    /**
//     * 设置出差申请的 流程变量
//     * @param id
//     * @return
//     */
//    @Override
//    public Map<String, Object> setvariables(Long id)
//    {
//        BeAwayApplyMapper evection = this.findOne(id);
//        //设置流程变量
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("assignee0",1);
//        variables.put("assignee1",2);
//        variables.put("assignee2",3);
//        variables.put("assignee3",4);
//        variables.put("evection",evection);
//        return variables;
//    }
//
//    @Override
//    public void startRunTask(Long id) {
//        evectionMapper.startTask(id);
//    }
//
//    @Override
//    public void endRunTask(Long id) {
//        evectionMapper.endTask(id);
//    }
//
//
//}
