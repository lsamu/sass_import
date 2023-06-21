<template>
    <div>
        <Editor :api-key="apiKey" :init="conf"></Editor>
    </div>
</template>
<script lang="ts" setup>

import Editor from "@tinymce/tinymce-vue"

const apiKey = "qagffr3pkuv17a8on1afax661irst1hbr4e6tbv888sz91jc";

const conf = {
    menubar: false,
    language: 'zh_CN',
    //可根据需求增加或删除插件
    plugins: 'preview searchreplace autolink directionality visualblocks visualchars fullscreen image   template  codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help emoticons autosave ', //字符串方式
    //toolbar为分好组的工具，可自定义分类 用 | 分割
    toolbar: ' undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough  anchor | alignleft aligncenter alignright alignjustify outdent indent | \
                styleselect formatselect fontselect fontsizeselect | table image  charmap emoticons hr pagebreak |bullist numlist | blockquote subscript superscript removeformat | \
                 insertdatetime print preview | fullscreen | bdmap indent2em lineheight formatpainter axupimgs',
}
</script>
