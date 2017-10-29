<#macro body >
<#if !LOGIN_USER.inner>
	<@pui.body>
		<#nested>
	</@pui.body>
<#else>
	<@pui.body>
		<#nested>
	</@pui.body>
</#if>
</#macro>

<#macro body2 >
<#if !LOGIN_USER.inner>
	<@pui.body>
		<#nested>
	</@pui.body>
<#else>
	<@pui.body>
		<#nested>
	</@pui.body>
</#if>
</#macro>