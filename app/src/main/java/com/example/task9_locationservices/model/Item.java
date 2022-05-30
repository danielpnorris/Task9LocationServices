package com.example.task9_locationservices.model;

public class Item {
    private int _itemId;
    private String _name;
    private String _phone;
    private String _description;
    private String _date;
    private String _location;
    private String _found;
    private String _place;

    public Item(String name, String phone, String description, String date, String location, String found, String place){
        this._name = name;
        this._phone = phone;
        this._description = description;
        this._date = date;
        this._location = location;
        this._found = found;
        this._place = place;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_itemId() {
        return _itemId;
    }

    public void set_itemId(int _itemId) {
        this._itemId = _itemId;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String is_found() {
        return _found;
    }

    public void set_found(String _found) {
        this._found = _found;
    }
    public String get_place(){ return _place; }
}
