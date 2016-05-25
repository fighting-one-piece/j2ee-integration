<table class="tableEdit" style="width:580px;" cellspacing="0" border="0" cellpadding="0">
	<#assign i = 0>
    <#list list as field>
    <#assign i = i + 1 >
    <#if field_index % 2 = 0>
	<tr>
	</#if>
		<td class="tdEditLabel" >${field_index+1}</td>			
		<td class="tdEditContent">${field.userName}
		</td>
	<#if i = 2 || !field_has_next>
	<#assign i = 0>
	</tr>
	</#if>
	</#list>
</table>