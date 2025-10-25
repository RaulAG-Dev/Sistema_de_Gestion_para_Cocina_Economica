package com.example.persistencia;

import org.json.simple.JSONObject;

public interface ConvertidorJSON<T> {
    JSONObject aJSON(T objeto);
    T deJSON(JSONObject jsonobjct);
}
