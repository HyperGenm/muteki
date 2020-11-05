module.exports = {
    presets: [
        '@vue/cli-plugin-babel/preset'
    ],
    "plugins": [
        //按需加载ant-design-vue
        ["import", {
            "libraryName": "ant-design-vue",
            "libraryDirectory": "es",
            "style": "css"
        }]
    ]
}