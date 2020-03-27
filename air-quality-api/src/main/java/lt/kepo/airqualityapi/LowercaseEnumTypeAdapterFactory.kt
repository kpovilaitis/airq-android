package lt.kepo.airqualityapi

//import com.google.gson.Gson
//import com.google.gson.TypeAdapter
//import com.google.gson.TypeAdapterFactory
//import com.google.gson.reflect.TypeToken
//import com.google.gson.stream.JsonReader
//import com.google.gson.stream.JsonToken
//import com.google.gson.stream.JsonWriter
//import java.io.IOException
//import java.util.*
//import kotlin.collections.HashMap
//
//class LowercaseEnumTypeAdapterFactory : TypeAdapterFactory {
//    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
//        val rawType = type.rawType as Class<T>
//
//        if (!rawType.isEnum) {
//            return null
//        }
//
//        val lowercaseToConstant = HashMap<String, T>()
//
//        for (constant in rawType.enumConstants!!) {
//            lowercaseToConstant[toLowercase(constant!!)] = constant
//        }
//
//        return object : TypeAdapter<T>() {
//            @Throws(IOException::class)
//            override fun write(out: JsonWriter, value: T?) {
//                if (value == null) {
//                    out.nullValue()
//                } else {
//                    out.value(toLowercase(value))
//                }
//            }
//
//            @Throws(IOException::class)
//            override fun read(reader: JsonReader): T? {
//                return if (reader.peek() === JsonToken.NULL) {
//                    reader.nextNull()
//                    null
//                } else {
//                    lowercaseToConstant[reader.nextString()]
//                }
//            }
//        }
//    }
//
//    private fun toLowercase(constant: Any): String {
//        return constant.toString().toLowerCase(Locale.US)
//    }
//}