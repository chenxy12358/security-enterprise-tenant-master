<#assign editor=false/>
<template>
  <el-dialog v-model="visible" :title="!dataForm.${pk.attrName} ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
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
      <el-form-item label="${column.comment!}" prop="${column.attrName}" style="height: 300px">
        <editor v-model="dataForm.${column.attrName}" :init="{ height: 300 }"></editor>
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
    <template v-slot:footer>
      <el-button @click="visible = false">{{ $t("cancel") }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t("confirm") }}</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";
import baseService from "@/service/baseService";
import { useDebounce } from "@/utils/utils";
<#if editor>
import Tinymce from "@/components/tinymce";
</#if>
export default defineComponent({
  <#if editor>
  components: { editor: Tinymce },
  </#if>
  setup() {
    return reactive({
      visible: false,
      dataForm: {
        <#list columnList as column>
        ${column.attrName}: "",
        </#list>
      }
    });
  },
  computed: {
    dataRule() {
      return {
        <#list columnList as column>
        <#if column.form && column.required>
        ${column.attrName}: [{ required: true, message: this.$t("validate.required"), trigger: "blur" }],
        </#if>
        </#list>
      };
    }
  },
  created() {
    this.dataFormSubmitHandle = useDebounce(this.dataFormSubmitHandle);
  },
  methods: {
    init() {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataFormRef"].resetFields();
        if (this.dataForm.${pk.attrName}) {
          this.getInfo();
        }
      });
    },
    // 获取信息
    getInfo() {
      baseService.get("/${moduleName}/${classname}/" + this.dataForm.${pk.attrName}).then((res) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg);
        }
        this.dataForm = res.data;
      });
    },
    // 表单提交
    dataFormSubmitHandle() {
      this.$refs["dataFormRef"].validate((valid: boolean) => {
        if (!valid) {
          return false;
        }
        (!this.dataForm.${pk.attrName} ? baseService.post : baseService.put)("/${moduleName}/${classname}", this.dataForm).then((res) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
          this.$message({
            message: this.$t("prompt.success"),
            type: "success",
            duration: 500,
            onClose: () => {
              this.visible = false;
              this.$emit("refreshDataList");
            }
          });
        });
      });
    }
  }
});
</script>