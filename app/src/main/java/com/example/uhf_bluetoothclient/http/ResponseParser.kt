package com.example.uhf_bluetoothclient.http

import com.example.uhf_bluetoothclient.entity.ResponseBean
import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type

@Parser(name = "Response", wrappers = [ResponseBean::class])
open class ResponseParser<T> : TypeParser<T> {
    //该构造方法是必须的
    protected constructor() : super()

    //如果依赖了RxJava，该构造方法也是必须的
    constructor(type: Type) : super(type)


    @Throws(IOException::class)
    override fun onParse(response: Response): T {
        //将okhttp3.Response转换为自定义的Response对象
        val responseData: ResponseBean<T?> = response.convertTo(ResponseBean::class, *types)
        var t: T? = responseData.data //获取data字段
        if (t == null && types[0] === String::class.java) {
            /*
             * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            @Suppress("UNCHECKED_CAST")
            t = responseData as? T
        }
        if (t == null) { //说明数据不正确，抛出异常
            throw ParseException(response.code.toString(), responseData.msg, response)
        }
        if (responseData.code!=200) {
            throw CommonException(responseData.msg)
        }
        return t //获取data字段
    }
}