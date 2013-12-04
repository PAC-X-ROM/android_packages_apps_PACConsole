package com.pac.console.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pac.console.adapters.changeItemType;

public class ChangeLogParser {
	public static ArrayList<changeItemType> ChangeLogParser(String JsonData)
			throws JSONException {
		JSONArray jsonArray = new JSONArray(JsonData);
		ArrayList<changeItemType> JSONArray = new ArrayList<changeItemType>();
		for (int i = 0; i < jsonArray.length(); i++) {
			changeItemType hold = new changeItemType();
			JSONObject json_data = jsonArray.getJSONObject(i);
			hold.setSHA(json_data.getString("SHA"));
			hold.setTitle(json_data.getString("Repository"));
			hold.setCaption(json_data.getString("Message"));
			hold.setDate(json_data.getString("CommitDate"));
			hold.setAuthor(json_data.getString("GitUsername"));

			JSONArray.add(hold);
		}
		ArrayList<changeItemType> formatedArray = new ArrayList<changeItemType>();
		String str_date = "";
		for (int i = 0; i < JSONArray.size(); i++){
			if (!str_date.equals(JSONArray.get(i).getDate().substring(0, 10))){
				str_date = JSONArray.get(i).getDate().substring(0, 10);
				changeItemType hold = new changeItemType();
				hold.setDate(JSONArray.get(i).getDate().substring(0, 10));
				hold.setIsHeader(true);
				formatedArray.add(hold);				
			}
			changeItemType hold2 = new changeItemType();
			hold2.setSHA(JSONArray.get(i).getSHA());
			hold2.setTitle(JSONArray.get(i).getTittle());
			hold2.setCaption(JSONArray.get(i).getCaption());
			hold2.setDate(JSONArray.get(i).getDate());
			hold2.setAuthor(JSONArray.get(i).getAuthor());

			formatedArray.add(hold2);
		}
		return formatedArray;

	}
}
