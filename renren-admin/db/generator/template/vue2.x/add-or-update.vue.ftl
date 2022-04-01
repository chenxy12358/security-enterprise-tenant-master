<#assign editor=false/>
<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.${pk.attrName} ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
<#list columnList as column>
  <#if column.form>
      <#if column.formType == 'text'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-input v-model="dataForm.${column.attrName}" placeholder="${column.comment!}"></el-input>
      </el-form-item>
      <#elseif column.formType == 'textarea'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-input type="textarea" v-model="dataForm.${column.attrName}"></el-input>
      </el-form-item>
      <#elseif column.formType == 'editor'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <!-- 富文本编辑器, 容器 -->
        <div id="J_quillEditor"></div>
        <!-- 自定义上传图片功能 (使用element upload组件) -->
        <el-upload
          :action="uploadUrl"
          :show-file-list="false"
          :before-upload="uploadBeforeUploadHandle"
          :on-success="uploadSuccessHandle"
          style="display: none;">
          <el-button ref="uploadBtn" type="primary" size="small">{{ $t('upload.button') }}</el-button>
        </el-upload>
      </el-form-item>
      <#assign editor=true/>
      <#assign editorName="${column.attrName}"/>
      <#elseif column.formType == 'select'>
      <#if column.dictName??>   
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <ren-select v-model="dataForm.${column.attrName}" dict-type="${column.dictName}" placeholder="${column.comment!}"></ren-select>
      </el-form-item>
      <#else>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-select v-model="dataForm.${column.attrName}" placeholder="请选择">
          <el-option label="人人" value="0"></el-option>
        </el-select>
      </el-form-item>
      </#if>
      <#elseif column.formType == 'radio'>
      <#if column.dictName??>   
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <ren-radio-group v-model="dataForm.${column.attrName}" dict-type="${column.dictName}"></ren-radio-group>
      </el-form-item>
      <#else>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-radio-group v-model="dataForm.${column.attrName}">
          <el-radio :label="0">启用</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      </#if>
      <#elseif column.formType == 'checkbox'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-checkbox-group v-model="dataForm.${column.attrName}">
          <el-checkbox label="启用" name="type"></el-checkbox>
          <el-checkbox label="禁用" name="type"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <#elseif column.formType == 'date'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-date-picker type="date" placeholder="${column.comment!}" value-format="yyyy-MM-dd" v-model="dataForm.${column.attrName}"></el-date-picker>
      </el-form-item>
      <#elseif column.formType == 'datetime'>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-date-picker type="datetime" placeholder="${column.comment!}" value-format="yyyy-MM-dd HH:mm:ss" v-model="dataForm.${column.attrName}"></el-date-picker>
      </el-form-item>
      <#else>
      <el-form-item label="${column.comment!}" prop="${column.attrName}">
        <el-input v-model="dataForm.${column.attrName}" placeholder="${column.comment!}"></el-input>
      </el-form-item>
      </#if>
  </#if>
</#list>
    </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
<#if editor>
import Cookies from 'js-cookie'
import 'quill/dist/quill.snow.css'
import Quill from 'quill'
</#if>
export default {
  data () {
    return {
      visible: false,
      <#if editor>
      quillEditor: null,
      quillEditorToolbarOptions: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block', 'image'],
        [{ 'header': 1 }, { 'header': 2 }],
        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
        [{ 'script': 'sub' }, { 'script': 'super' }],
        [{ 'indent': '-1' }, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        [{ 'size': ['small', false, 'large', 'huge'] }],
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['clean']
      ],
      uploadUrl: '',
      </#if>
      dataForm: {
<#list columnList as column>
        ${column.attrName}: '',
</#list>
      }
    }
  },
  computed: {
    dataRule () {
      <#if editor>
      const validate${editorName} = (rule, value, callback) => {
        if (this.quillEditor.getLength() <= 1) {
          return callback(new Error(this.$t('validate.required')))
        }
        callback()
      }
      </#if>
      return {
<#list columnList as column>
<#if column.form && column.required>
        <#if editor && column.attrName == editorName>
        ${column.attrName}: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' },
          { validator: validate${editorName}, trigger: 'blur' }
        ],
        <#else>
        ${column.attrName}: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        </#if>
</#if>
</#list>
      }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        <#if editor>
        if (this.quillEditor) {
          this.quillEditor.deleteText(0, this.quillEditor.getLength())
        } else {
          this.quillEditorHandle()
        }
        </#if>
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.${pk.attrName}) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get('/${moduleName}/${classname}/' + this.dataForm.${pk.attrName}).then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
        <#if editor>
        this.quillEditor.root.innerHTML = this.dataForm.${editorName}
        </#if>
      }).catch(() => {})
    },
    <#if editor>
    // 富文本编辑器
    quillEditorHandle () {
      this.quillEditor = new Quill('#J_quillEditor', {
        modules: {
          toolbar: this.quillEditorToolbarOptions
        },
        theme: 'snow'
      })
      // 自定义上传图片功能 (使用element upload组件)
      this.uploadUrl = window.SITE_CONFIG['apiURL'] + '/sys/oss/upload?token=' + Cookies.get('token')
      this.quillEditor.getModule('toolbar').addHandler('image', () => {
        this.$refs.uploadBtn.$el.click()
      })
      // 监听内容变化，动态赋值
      this.quillEditor.on('text-change', () => {
        this.dataForm.${editorName} = this.quillEditor.root.innerHTML
      })
    },
    // 上传图片之前
    uploadBeforeUploadHandle (file) {
      if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        this.$message.error('只支持jpg、png、gif格式的图片！')
        return false
      }
    },
    // 上传图片成功
    uploadSuccessHandle (res, file, fileList) {
      if (res.code !== 0) {
        return this.$message.error(res.msg)
      }
      this.quillEditor.insertEmbed(this.quillEditor.getSelection().index, 'image', res.data.src)
    },
    </#if>
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.${pk.attrName} ? 'post' : 'put']('/${moduleName}/${classname}/', this.dataForm).then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
