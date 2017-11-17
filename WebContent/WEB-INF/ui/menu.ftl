<#macro menu url>
	<div class="sidebar" id="sidebar">
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
		</script>
		<ul class="nav nav-list">
			<#list menuList as menu>
				
					<#if permission[menu.code]??>
					<li>
						<#if menu.path == "#">
							<style>
								.nav-list > li > a > img#menu_${menu.id} {
									border:0;
								    background-image: url(${ctx}/assets/img2/${menu.icon!}.png);
								}
								
								.nav-list > li.open > a > img#menu_${menu.id} {
									border:0;
								    background-image: url(${ctx}/assets/img2/${menu.icon!}_X.png);
								}
							</style>
							<a href="#" class="dropdown-toggle">
								<img id='menu_${menu.id}' src='${ctx}/assets/img2/tm.png' style="padding-top: 0px;height: 30px; width:30px" />
								<span class="menu-text"> &nbsp;&nbsp;&nbsp;&nbsp;${i18n.get('${menu.name}')}</span>
								<b class="arrow icon-angle-down"></b>
							</a>
							<#if menu.childMenuList?size gt 0>
								<ul class="submenu">
									<li>
										<#list menu.childMenuList as menu2>
											<#if permission[menu2.code]??>
												<#if menu2.path?? && menu2.path == "#">
													<a href="#" class="dropdown-toggle">
														<i class="icon-list"></i>
														<span class="menu-text"> ${i18n.get('${menu2.name}')}</span>
														<b class="arrow icon-angle-down"></b>
													</a>
													<#if menu2.childMenuList?size gt 0>
														<ul class="submenu">
															<li>
																<#list menu2.childMenuList as menu3>
																	<#if permission[menu3.code]?? && menu3.path!='#'>
																		<a href="#2" onClick="addTab('${i18n.get('${menu3.name}')}','${ctx}${menu3.path}')">
																			<i class="icon-double-angle-right"></i>
																			${i18n.get('${menu3.name}')}
																		</a>
																	</#if>
																</#list>
															</li>
														</ul>
													</#if>
												<#else>
													<a href="#3" onClick="addTab('${i18n.get('${menu2.name}')}','${ctx}${menu2.path!''}')">
														<i class="icon-double-angle-right"></i>
														${i18n.get('${menu2.name}')}
													</a>
												</#if>
											</#if>
										</#list>
									</li>
								</ul>
							</#if>
						</#if>
						</li>
					</#if>
			</#list>
			
		</ul><!-- /.nav-list -->
		<div class="sidebar-collapse" id="sidebar-collapse">
			<i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
		</div>
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
		</script>
	</div>

</#macro>