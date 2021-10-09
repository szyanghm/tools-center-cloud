package com.tools.center.activiti;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tools.center.FlowService;
import com.tools.center.dto.FlowDto;
import com.tools.center.utils.FileUtil;
import com.tools.center.utils.SpringContextUtil;
import com.tools.center.vo.FileVO;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@RestController
@Scope("prototype")
@RequestMapping("/activiti")
@Slf4j
public class ActivitiCreateRestResource {

    @Autowired
    private FlowService flowService;
    @Autowired
    ProcessEngineFactoryBean processEngineFactoryBean;
    /**
     * 创建模型
     */
    @PostMapping(value = "create", produces = {"application/json;charset=UTF-8"})
    public ResultVO create(@RequestBody FlowDto dto) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, dto.getFlowName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            if(StringUtils.isBlank(dto.getDescription())){
                dto.setDescription("请输入流程描述信息~");
            }
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,dto.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setKey(dto.getFlowKey());
            modelData.setName(dto.getFlowName());
            //保存模型
            repositoryService.saveModel(modelData);
            dto.setState(false);
            dto.setModelId(modelData.getId());
            flowService.saveOrUpdateFlow(dto);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            // 编辑流程模型时,只需要直接跳转此url并传递上modelId即可
            Map<String,String> map = new HashMap<>();
            map.put("modelId",modelData.getId());
            return ResultVO.success(map);
        } catch (Exception e) {
            log.error("创建模型失败：");
        }
        return ResultVO.fail();
    }

    /**
     * 下载流程定义文件：包含svg图片和xml文件
     * @param dto
     * @param response
     */
    @PostMapping(value = "/download")
    public void downloadFlowFile(@RequestBody FlowDto dto, HttpServletResponse response){
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(dto.getFlowId()).singleResult();
        if(ObjectUtil.isNotNull(pd)&&StringUtils.isNotBlank(pd.getId())) {
            //获取BpmnModel对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
            DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
            //创建转换对象w
            BpmnXMLConverter converter = new BpmnXMLConverter();
            //把bpmnModel对象转换成字符
            byte[] bytes = converter.convertToXML(bpmnModel);
            InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "宋体", "宋体", "宋体");
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String zipName = dto.getFlowName() + ".zip";
            String svg = dto.getFlowName() + ".svg";
            String xml = dto.getFlowName() + ".xml";
            InputStream xmlInputStream = new ByteArrayInputStream(bytes);
            List<FileVO> list = new ArrayList<>();
            list.add(new FileVO(svg, inputStream));
            list.add(new FileVO(xml, xmlInputStream));
            FileUtil.downloadZip(path, zipName, list, response);
        }
    }

}
