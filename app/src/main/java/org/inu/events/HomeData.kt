package org.inu.events

//TODO("서버에서 데이터 넘겨주는거에 따라서 내용 수정해야함 ")
data class HomeData(var title:String,
                    var date:String,
                    var institution:String,
                    var imageResId: Int,
                    var dateColor: Int)