package com.ibm.kstar.action.flow.design;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FlowUtils {

	public static void putValue(JSONObject json,String key,String value){
		JSONObject o = new JSONObject();
		o.put("value", value);
		json.put(key, o);
	}
	
	public static void putText(JSONObject json,String key,String value){
		JSONObject o = new JSONObject();
		o.put("text", value);
		json.put(key, o);
	}
	
	@SuppressWarnings({"unchecked" })
	public static String xmlToJson(String xml) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		JSONObject flow = new JSONObject();
		
		JSONObject processProps = new JSONObject();
		JSONObject states = new JSONObject();
		JSONObject paths = new JSONObject();
		
		flow.put("props", processProps);
		flow.put("states",states);
		flow.put("paths",paths);
		
		JSONObject props = new JSONObject();
		putValue(props,"name",root.attributeValue("name"));
		putValue(props,"module",root.attributeValue("module"));
		processProps.put("props", props);
		
		List<Element> activitys = root.elements("activity");
		for(Element activity : activitys){
			JSONObject state = new JSONObject();
			state.put("type", activity.attributeValue("type"));
			putText(state, "text", activity.attributeValue("name"));
			JSONObject attr = new JSONObject();
			attr.put("x", Integer.parseInt(activity.attributeValue("x")));
			attr.put("y", Integer.parseInt(activity.attributeValue("y")));
			attr.put("width", Integer.parseInt(activity.attributeValue("width")));
			attr.put("height", Integer.parseInt(activity.attributeValue("height")));
			state.put("attr", attr);
			states.put(activity.attributeValue("id"), state);
			
			//--------------------------------------------------------------------
			JSONObject participantsProps = new JSONObject();
			state.put("props", participantsProps);
			putValue(participantsProps, "text", activity.attributeValue("name"));
			//--------------------------------------------------------------------
			if("task".equals(activity.attributeValue("type"))){
				josnToXMLParticipants(activity, participantsProps);
				
				String reject = activity.attributeValue("reject");
				String message = activity.attributeValue("message");
				String close = activity.attributeValue("close");
				String nonauto = activity.attributeValue("nonauto");
				
				putValue(participantsProps, "reject", reject);
				putValue(participantsProps, "message", message);
				putValue(participantsProps, "close", close);
				putValue(participantsProps, "nonauto", nonauto);
				
			} else if("multiTask".equals(activity.attributeValue("type")) || "notice".equals(activity.attributeValue("type"))){
				josnToXMLParticipants(activity, participantsProps);
			}else if("decision".equals(activity.attributeValue("type"))){
				Element expression = activity.element("expression");
				JSONObject expressionProps = new JSONObject();
				state.put("props", expressionProps);
				putValue(expressionProps, "expression", expression.getText());
			}
			
			//--------------------------------------------------------------------
			List<Element> transitions = activity.elements("transition");
			int index = 0;
			for(Element transition : transitions){
				index++;
				String to = transition.attributeValue("to");
				String name = transition.attributeValue("name");
				String from = activity.attributeValue("id");
				
				JSONObject path = new JSONObject();
				paths.put("path_"+ from +"_"+ index, path);
				path.put("from", from);
				path.put("to", to);
				JSONArray dots = new JSONArray();
				path.put("dots", dots);
				putText(path, "text", name);
				
				JSONObject pathProps = new JSONObject();
				putValue(pathProps, "text", name);
				path.put("props", pathProps);
				
				Element ds = transition.element("dots");
				List<Element> dotList = ds.elements("dot");
				for(Element dot : dotList){
					JSONObject o = new JSONObject();
					o.put("x", Integer.parseInt(dot.attributeValue("x")));
					o.put("y", Integer.parseInt(dot.attributeValue("y")));
					dots.add(o);
				}
			}
		}
		
		return flow.toJSONString();
	}

	private static void josnToXMLParticipants(Element activity, JSONObject participantsProps) {
		Element participants = activity.element("participants");
		
		List<Element> participantList = participants.elements("participant");
		String ids = "";
		String types = "";
		String names = "";
		for(Element participant : participantList){
			ids = ids + participant.attributeValue("id") + ",";
			types = types + participant.attributeValue("type")+ ",";
			names = names + participant.attributeValue("name")+ ",";
		}
		putValue(participantsProps, "ids", ids);
		putValue(participantsProps, "types", types);
		putValue(participantsProps, "names", names);
		String participantType = activity.attributeValue("participantType");
		
		if(participantList.size() > 0 && participantList.get(0)!=null){
			Element sqlElement = participantList.get(0);
			String sql = sqlElement.getText();
			sql = sql.replaceAll("'", "\\\\'");
			putValue(participantsProps, "sql", sql);
		}
		
		putValue(participantsProps, "participantType", participantType);
	}
	
	public static String jsonToXml(String json,String ctx) throws Exception{
		
		JSONObject flow = JSON.parseObject(json);
		JSONArray states = (JSONArray) flow.get("states");
		JSONArray paths = (JSONArray) flow.get("paths");
	
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("process");
		doc.setRootElement(root);
		
		JSONObject processProps = (JSONObject)flow.get("props");
		String module = (String)processProps.get("module");
		String name = (String)processProps.get("name");
		root.addAttribute("module", module);
		root.addAttribute("name", name);
		
		for(int i=0;i<states.size();i++){
			JSONObject state = (JSONObject)states.get(i);
			Element activity = DocumentHelper.createElement("activity");
			activity.addAttribute("id", state.getString("id"));
			activity.addAttribute("type", state.getString("type"));
			activity.addAttribute("name",state.getString("text"));
			activity.addAttribute("x", state.getString("x"));
			activity.addAttribute("y", state.getString("y"));
			activity.addAttribute("width", state.getString("width"));
			activity.addAttribute("height", state.getString("height"));
			
			JSONObject prop = (JSONObject)state.get("props");
			if("task".equals(state.getString("type"))){
				if(StringUtil.isNotEmpty(prop.getString("reject"))){
					activity.addAttribute("reject", prop.getString("reject"));
				}
				if(StringUtil.isNotEmpty(prop.getString("message"))){
					activity.addAttribute("message", prop.getString("message"));
				}
				if(StringUtil.isNotEmpty(prop.getString("close"))){
					activity.addAttribute("close", prop.getString("close"));
				}
				if(StringUtil.isNotEmpty(prop.getString("nonauto")) && "Y".equals(prop.getString("nonauto"))){
					activity.addAttribute("nonauto", prop.getString("nonauto"));
				}else{
					activity.addAttribute("nonauto", "N");
				}
			}
			
			if("task".equals(state.getString("type")) || "multiTask".equals(state.getString("type")) || "notice".equals(state.getString("type"))){
				if(StringUtil.isNotEmpty(prop.getString("participantType"))){
					activity.addAttribute("participantType", prop.getString("participantType"));
				}
				Element participants = DocumentHelper.createElement("participants");
				String type = null;
				if("1".equals(prop.getString("participantType"))){
					type = "assign";
					String types = prop.getString("types");
					String ids = prop.getString("ids");
					String names = prop.getString("names");
					if(StringUtil.isNotEmpty(ids)){
						String[] _ids = ids.split(",");
						String[] _types = types.split(",");
						String[] _names = names.split(",");
						for(int k=0;k<_ids.length;k++){
							if(StringUtil.isNotEmpty(_ids[k])){
								Element participant = DocumentHelper.createElement("participant");
								participant.addAttribute("id", _ids[k]);
								participant.addAttribute("type", _types[k]);
								participant.addAttribute("name", _names[k]);
								participants.add(participant);
							}
						}
					}
				}else if("2".equals(prop.getString("participantType"))){
					type = "creator";
				}else if("3".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/directLeadership.html");
				}else if("4".equals(prop.getString("participantType"))){
					type = "runtimeAssign";
				}else if("11".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/level1.html");
				}else if("12".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/level2.html");
				}else if("13".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/level3.html");
				}else if("14".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/level4.html");
				}else if("15".equals(prop.getString("participantType"))){
					type = "expand";
					participants.addAttribute("url", ctx+"/participant/level5.html");
				}else if("21".equals(prop.getString("participantType"))){
					type = "positionInForm";
				}else if("22".equals(prop.getString("participantType"))){
					type = "employeeInForm";
				}else if("23".equals(prop.getString("participantType"))){
					type = "positionInForm2";
				}else if("24".equals(prop.getString("participantType"))){
					type = "employeeInForm2";
				}else if("99".equals(prop.getString("participantType"))){
					type = "sql";
					String sql = prop.getString("sql");
					Element participant = DocumentHelper.createElement("participant");
					participant.addText(sql);
					participants.add(participant);
				}else{
					type = "error";
				}
				participants.addAttribute("type", type);
				activity.add(participants);
			}
			else if("decision".equals(state.getString("type"))){
				Element expression = DocumentHelper.createElement("expression");
				expression.addText(prop.getString("expression"));
				activity.add(expression);
			}
			root.add(activity);
			for(int j=0;j<paths.size();j++){
				JSONObject path = (JSONObject)paths.get(j);
				if(state.getString("id").equals(path.getString("from"))){
					Element transition = DocumentHelper.createElement("transition");
					transition.addAttribute("to", path.getString("to"));
					transition.addAttribute("name", path.getString("text"));
					transition.addAttribute("id", path.getString("text"));
					activity.add(transition);
					JSONArray dots = (JSONArray)path.get("dots");
					Element _dots = DocumentHelper.createElement("dots");
					transition.add(_dots);
					for(int k=0;k<dots.size();k++){
						JSONObject dot = (JSONObject)dots.get(k);
						String x = dot.getString("x");
						String y = dot.getString("y");
						Element _dot = DocumentHelper.createElement("dot");
						_dot.addAttribute("x", x);
						_dot.addAttribute("y", y);
						_dots.add(_dot);
					}
				}
			}
		}
		
		return doc.asXML();
	}
	
}
