package example.com.diary.domain.models

data class Translation(var langFrom: String,
                      var textFrom: String,
                      var langTo: String,
                      var textTo: String,
                      var isFavourite: Boolean,
                      var id: Long = 0)