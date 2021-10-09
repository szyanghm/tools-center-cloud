package com.tools.center.activiti;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tools.center.FlowService;
import com.tools.center.dto.FlowDto;
import com.tools.center.vo.FlowVO;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
public class ModelSaveRestResource implements ModelDataJsonConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = {"/service/model/{modelId}/save"}, method = {org.springframework.web.bind.annotation.RequestMethod.PUT})
    @ResponseStatus(HttpStatus.OK)
    public void saveModel(@PathVariable String modelId, @RequestParam MultiValueMap<String, String> values) {
        try {
            Model model = this.repositoryService.getModel(modelId);
            System.out.println("ModelSaveRestResource.saveModel----------");
            ObjectNode modelJson = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());
            modelJson.put("name", values.getFirst("name"));
            modelJson.put("description", values.getFirst("description"));
            model.setMetaInfo(modelJson.toString());
            model.setName(values.getFirst("name"));
            this.repositoryService.saveModel(model);
            String jsonXml = json_xml(values.getFirst("json_xml"),"");
            FlowDto dto = new FlowDto();
            if(jsonXml.equals("未定义")){
                FlowVO vo = flowService.findKey(modelId);
                jsonXml = json_xml(values.getFirst("json_xml"),vo.getFlowKey());
                dto.setFlowKey(vo.getFlowKey());
            }else {
                dto.setFlowKey(jsonXml);
                jsonXml = values.getFirst("json_xml");
            }
            dto.setModelId(model.getId());
            dto.setFlowName(values.getFirst("name"));
            flowService.updateByModelId(dto);
            this.repositoryService.addModelEditorSource(model.getId(), (jsonXml).getBytes("utf-8"));
            InputStream svgStream = new ByteArrayInputStream((values.getFirst("svg_xml")).getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            transcoder.transcode(input, output);
            byte[] result = outStream.toByteArray();
            this.repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception e) {
            LOGGER.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }

    public static void main(String[] args) {
        String  str ="{\"resourceId\":\"c517ac56-f688-11eb-81e1-005056c00001\",\"properties\":{\"process_id\":\"555555\",\"name\":\"\",\"documentation\":\"\",\"process_author\":\"\",\"process_version\":\"\",\"process_namespace\":\"http://www.activiti.org/processdef\",\"executionlisteners\":\"\",\"eventlisteners\":\"\",\"signaldefinitions\":\"\",\"messagedefinitions\":\"\"},\"stencil\":{\"id\":\"BPMNDiagram\"},\"childShapes\":[],\"bounds\":{\"lowerRight\":{\"x\":1200,\"y\":1050},\"upperLeft\":{\"x\":0,\"y\":0}},\"stencilset\":{\"url\":\"stencilsets/bpmn2.0/bpmn2.0.json\",\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"},\"ssextensions\":[]}";
//        JSONObject jsonObject = JSONObject.parseObject(str);
//        JSONObject jobj = jsonObject.getJSONObject("properties");
//        jobj.put("process_id","1111111");
//        System.out.println(jobj.getString("process_id"));
//        jsonObject.put("properties",jobj);
//        String ss = JSONObject.toJSONString(jsonObject);
        System.out.println(json_xml(str,"222222"));
    }

    public static String json_xml(String jsonXml,String processId){
        JSONObject jsonObject = JSONObject.parseObject(jsonXml);
        JSONObject properties = jsonObject.getJSONObject("properties");
        String process_id = properties.getString("process_id");
        if(StringUtils.isBlank(processId)){
            return process_id;
        }
        properties.put("process_id",processId);
        jsonObject.put("properties",properties);
        String jsonStr = JSONObject.toJSONString(jsonObject);
        return jsonStr;
    }
}
