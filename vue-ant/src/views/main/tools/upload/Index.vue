<template>
    <upload action="/tools/upload"
            :fileList="fileList"
            @success="success"></upload>
</template>

<script>
    import Upload from '@/components/upload/Index.vue';
    import {ref} from 'vue';

    export default {
        components: {
            'upload': Upload
        },
        setup() {
            /**
             * 所有成功的图片
             * @param file
             * @param fileList
             */
            const success = (file, fileList) => {
                console.log(file)
                console.log(fileList)
            }

            let fileList = ref([
                {
                    uid: '-1',
                    name: 'xxx.png',
                    status: 'done',
                    url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
                    thumbUrl: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
                },
            ]);

            return {
                success,
                fileList
            }
        }
    };
</script>
