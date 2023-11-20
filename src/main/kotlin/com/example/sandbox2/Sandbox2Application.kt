package com.example.sandbox2

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

// Spring徹底入門 p19
// Bean定義1
// Sandbox2Application（@SprintBootApplicationアノテーションを付与したクラス）が JavaベースConfiguration
// @SprintBootApplicationアノテーションが@Configurationアノテーションを包含（？という言い方が正しいのか？知らん）しているため、こうなる
// また、@SpringBootApplication は、コンポーネントスキャンするためのアノテーションも包含してる。
@SpringBootApplication
class Sandbox2Application {
    // ここでbean定義しちゃうことができる
    @Bean
    fun dummy001dayo(): Dummy001IF {
        return Dummy001()
    }
}

fun main(args: Array<String>) {
    // Spring徹底入門 p16
    // 返り値がApplicationContext（DIコンテナ）
    val ctx = runApplication<Sandbox2Application>(*args)
    // ApplicationContextの中のBean一覧を見てみる
    ctx.beanDefinitionNames.forEach {
        println("Bean名=${it}")
    }
    // Bean一覧をみると
    // いろんなBeanが登録されていることがわかる
    // これらのBeanは、SpringBootによって自動で登録されたものたち
    // 引用 https://qiita.com/kazuki43zoo/items/8645d9765edd11c6f1dd
    // SprintFrameworkを使用する場合、アプリケーションの動作に必要となるBean定義をいちいち全部定義する必要があるらしい
    // → 面倒臭い
    // SpringBootができた
    // → アプリケーションの動作に最低限必要となるであろうBean定義を自動でやる
    // Dummy001の依存解決してみる
    val dummy001 = ctx.getBean(Dummy001IF::class.java)
    println("dummy001の依存解決できました: ${dummy001.hoge()}")
    // Dummy002の依存解決してみる
    val dummy002 = ctx.getBean(Dummy002IF::class.java)
    println("dummy002の依存解決できました: ${dummy002.fuga()}")
    // Dummy003の依存解決してみる
    try {
        val dummy003 = ctx.getBean(Dummy003IF::class.java)
    } catch (e: NoSuchBeanDefinitionException) {
        println("dummy003の依存解決できません！Bean定義してないもん: ${e.message}")
    }
    // Dummy004の依存解決してみる
    val dummy004 = ctx.getBean(Dummy004IF::class.java)
    println("dummy004の依存解決できました: ${dummy004.bar()}")
}

@Controller
class AppController {
    @GetMapping("/")
    fun getIndex(): String {
        return "index"
    }
}

// Dummy001
interface Dummy001IF {
    fun hoge(): String
}

class Dummy001 : Dummy001IF {
    override fun hoge(): String {
        return "hoge"
    }
}

// Dummy002
interface Dummy002IF {
    fun fuga(): String
}

class Dummy002 : Dummy002IF {
    override fun fuga(): String {
        return "fuga"
    }
}

// Dummy003
interface Dummy003IF {
    fun foo(): String
}

class Dummy003 : Dummy003IF {
    override fun foo(): String {
        return "foo"
    }
}

// Bean定義2
// Spring徹底入門 p19
// @Configuration アノテーションが付与されたクラスは
// JavaベースのConfiguration
// 規模の大きいSpringBootアプリケーションでは、普通、こんな感じでBean定義する
// @SpringBootApplication が付与されたクラスでは、Bean定義をしない
@Configuration
class OtherBeanDef {
    // bean定義しちゃうぞ
    @Bean
    fun dummy002dayo(): Dummy002IF {
        return Dummy002()
    }
}

// Bean定義3
// Spring徹底入門 p21
// アノテーションベースのConfiguration
// 規模の大きいSpringBootアプリケーションでは、アノテーションベースのBean定義が、最もよく使われるのではないだろうか
// Dummy004
interface Dummy004IF {
    fun bar(): String
}

// @Component アノテーションを付与すると
// コンポーネントスキャンの際にBean定義される
@Component
class Dummy004 : Dummy004IF {
    override fun bar(): String {
        return "bar"
    }
}