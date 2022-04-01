package ${package}.modules.${moduleName}.dto<#if subModuleName??>.${subModuleName}</#if>;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
<#list imports as i>
import ${i!};
</#list>

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Data
@ApiModel(value = "${tableComment}")
public class ${ClassName}DTO implements Serializable {
    private static final long serialVersionUID = 1L;

<#list columnList as column>
    <#if column.comment!?length gt 0>
    @ApiModelProperty(value = "${column.comment}")
    </#if>
    private ${column.attrType} ${column.attrName};
</#list>

}