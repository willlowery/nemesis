package form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StandardForm implements Form {

    Map<String, FormElementParser> parsers;
    Map<String, List<Object>> params;
    

    public StandardForm() {
        params = new HashMap<>();
        parsers = new HashMap<>();
    }
    
    protected void define(String field, FormElementParser parser) {
       parsers.put(field, parser);
    }

    @Override
    public void map(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for(Map.Entry<String, String[]> ent : entrySet){
            List<Object> list = new ArrayList<>();            
           for(String value: ent.getValue()){
                FormElementParser parser = parsers.get(ent.getKey());
                try {
                    list.add(parser.parse(value, request.getLocale()));
                } catch (ParseException ex) {
                    
                }
           }
           params.put(ent.getKey(), list);
        }
    }

    protected <DesiredType> DesiredType get(String field) {
        List<Object> entry = params.get(field);
        if(entry == null){
            return null;
        }else{
            return (DesiredType) entry.get(0);
        }
        
    }

   protected <DesiredType> DesiredType getValues(String field) {
        if(params.get(field) == null){
            return (DesiredType) Collections.EMPTY_LIST;
        }
        return (DesiredType) params.get(field);
    }

}
