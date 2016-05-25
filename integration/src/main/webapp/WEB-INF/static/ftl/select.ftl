<select name="props.${field.formFieldName}">
	<#list field.formFieldItems as item>
	<option value=${item.formFieldItemValue}>${item.formFieldItemLabel}</option>
	</#list>
</select>