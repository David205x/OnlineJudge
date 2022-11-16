<template>
    <ContentField>
        <p class="fw-bold" style="font-size: 30px; ">
            这里有些码，里面有你的吗？
        </p>
        <div class="row">
            <div class="col-4"><highlightjs language="c_cpp" :code=code_cpp.context /></div>
            <div class="col-5"><highlightjs language="java" :code=code_java.context /></div>
            <div class="col-3">
                <highlightjs language="python" :code=code_python.context />
                <p style="color: white; margin:5vh 0 5vh 0">这里没你码</p>
            </div>
        </div>
        <div class="row">
            <div class="col-3">
                <highlightjs language="python" :code=code_python.context />
                <p style="color: white; margin:5vh 0 5vh 0">这里也没你码</p>
            </div>
            <div class="col-5"><highlightjs language="java" :code=code_java.context /></div>
            <div class="col-4"><highlightjs language="c_cpp" :code=code_cpp.context /></div>
        </div>

        <div class="row">
            <div class="col-8">
                <p class="font-monospace" style="font-size: 100px; color: #535353; margin: -5vh 0 -6vh 1vw">404 Not Found</p>
            </div>
            <div class="col-4" style="float: right">
                <p class="fw-bold" style="font-size: 30px; margin-top: 5vh;float: right">
                    找不到？看看主界面！
                    <a class="btn btn-outline-primary" @click="redirect">回主页</a>
                </p>

            </div>
        </div>

    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import hljsVuePlugin from "@highlightjs/vue-plugin";
import router from "@/router";

export default {
    name: "NotFoundView",
    components: {
        ContentField,
        highlightjs: hljsVuePlugin.component,
    },
    setup(){
        let code_cpp = {
            context:
                "#include< iostream >\n" +
                "using namespace std;\n" +
                "int main() {\n" +
                "    cout << \"i'm not your C++ code\";\n" +
                "    return 0;\n" +
                "}",
        }
        let code_java = {
            context:
                "public class Main{\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"i'm not your Java code\");\n" +
                "    }\n" +
                "}"
        }
        let code_python = {
            context:
            "print(\"i'm not your python code\")\n"
        }

        const redirect = () => {
            router.push({name: "main_page"})
        }
        return{
            code_cpp,
            code_java,
            code_python,

            redirect
        }
    }
}
</script>

<style scoped>

</style>