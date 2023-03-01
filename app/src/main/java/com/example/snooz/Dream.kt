package com.example.snooz

//class Dream {
//    var name: String = ""
//        set(value: String){ name = value  }
//    var tag = ""
//    var text = ""
//
//    constructor(name: String, tag: String, text: String){
//        this.name = name
//        this.tag= tag
//        this.text = text
//    }
//
//    constructor()
//
//
//}

data class Dream(val dreamId: String = "", val name:String = "", val tag: String = "", val text: String = "") {

}