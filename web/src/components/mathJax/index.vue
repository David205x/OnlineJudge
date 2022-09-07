<template>
    <div class="latexDiv" v-html="latex"/>
</template>
   
<script>
import MathJax from './MathJax.js'
export default {
    name: 'TheLatex2Math',
    props: {latex: {type: String}}, 
    created() { 
    this.mathJax()
    },
    watch: { 
    latex() {
        this.mathJax()
    }
    },
    methods: {
    mathJax () {
        this.$nextTick(function () { 
        if (MathJax.isMathjaxConfig) { 
            MathJax.initMathjaxConfig()
        }
        MathJax.MathQueue('latexDiv') 
        })
    }
    }
}
</script>