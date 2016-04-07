package org.almiso.taskreminder.utils;

import org.almiso.taskreminder.model.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alexandr Sosorev on 06.04.2016.
 */

public class TasksReminderUtils {

    public static final String TAG = "TasksReminderUtils";

    public static ArrayList<Task> convertStringToArray(String value) {
        Logger.d(TAG, "value=" + value);
        ArrayList<Task> result = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(value);
            if (object.has("Tasks")) {
                JSONArray array = object.getJSONArray("Tasks");
                for (int i = 0; i < array.length(); i++) {
                    if (array.getJSONObject(i).has("Task_item")) {
                        result.add(readTaskParams(array.getJSONObject(i).getJSONObject("Task_item").toString()));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public static String convertArrayToString(ArrayList<Task> array) {
        JSONArray resultArray = new JSONArray();
        for (Task task : array) {
            try {
                resultArray.put(new JSONObject().put("Task_item", writeTaskParams(task)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject object = new JSONObject();
        try {
            object.put("Tasks", resultArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    public static Task readTaskParams(String stream) {
        Task task = new Task();
        try {
            JSONObject object = new JSONObject(stream);

            if (object.has("id") && !object.get("id").equals("")) {
                task.id = object.getInt("id");
            }
            if (object.has("taskState") && !object.get("taskState").equals("")) {
                task.taskState = object.getInt("taskState");
            }
            if (object.has("value")) {
                task.value = object.getString("value");
            }
            if (object.has("priority") && !object.get("priority").equals("")) {
                task.priority = object.getInt("priority");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return task;
        }
        return task;
    }

    public static JSONObject writeTaskParams(Task task) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", task.id);
            object.put("taskState", task.taskState);
            object.put("value", task.value);
            object.put("priority", task.priority);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

}
