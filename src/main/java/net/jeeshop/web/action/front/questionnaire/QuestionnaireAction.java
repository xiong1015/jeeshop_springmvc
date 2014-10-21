package net.jeeshop.web.action.front.questionnaire;import java.io.IOException;import java.util.LinkedHashMap;import java.util.LinkedList;import java.util.List;import net.jeeshop.core.BaseAction;import net.jeeshop.core.FrontContainer;import net.jeeshop.services.front.questionnaire.QuestionnaireService;import net.jeeshop.services.front.questionnaire.bean.Questionnaire;import net.jeeshop.services.front.questionnaireItem.QuestionnaireItemService;import net.jeeshop.services.front.questionnaireItem.bean.QuestionnaireItem;import org.apache.commons.lang.StringUtils;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.context.annotation.Scope;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.RequestMapping;/** * 问卷调查 * @author huangf * */@Scope("prototype")   @Controller("frontQuestionnaireAction")   @RequestMapping("/questionnaire")  public class QuestionnaireAction extends BaseAction<Questionnaire> {	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionnaireAction.class);	private static final long serialVersionUID = 1L;		@Autowired	private QuestionnaireService questionnaireService;	@Autowired	private QuestionnaireItemService questionnaireItemService;	{		show = "/questionnaire";		SUCCESS = "redirect:/questionnaireSuccess.jsp";	}		@ModelAttribute	public void initStrutsActionParam(){		this.server = questionnaireService ;	}		protected void selectListAfter() {		pager.setPagerUrl("questionnaire/selectList.action");	}	public Questionnaire getE() {		return this.e;	}	public void prepare() throws Exception {		if (this.e == null) {			this.e = new Questionnaire();		}				setSelectMenu(FrontContainer.not_select_menu);//设置主菜单为不选中	}	public void insertAfter(Questionnaire e) {		e.clear();	}		private String getEditUrl(String id){		return "toEdit2.action?id="+id;	}		/**	 * 查看问卷	 * @return	 * @throws Exception	 */	@RequestMapping("/show")	public String show() throws Exception {		String id = request.getParameter("id");				logger.error("QuestionnaireAction show id = " + id);		if(StringUtils.isBlank(id)){			throw new NullPointerException("问卷ID不能为空！");		}				e = questionnaireService.selectById(id);		if(e==null){			throw new NullPointerException("根据问卷ID查询不到问卷信息！");		}				//加载问卷题目列表		QuestionnaireItem qItem = new QuestionnaireItem();		qItem.setQid(e.getId());				List<QuestionnaireItem> list = questionnaireItemService.selectList(qItem);		if(list!=null && list.size()>0){			if(e.getQuestionnaireItemMap()==null){				e.setQuestionnaireItemMap(new LinkedHashMap<String, QuestionnaireItem>());			}						for(int i=0;i<list.size();i++){				QuestionnaireItem item = list.get(i);								QuestionnaireItem mapItem = e.getQuestionnaireItemMap().get(item.getSubject());				if(mapItem==null){					if(item.getOptionList()==null){						item.setOptionList(new LinkedList<String>());					}					item.getOptionList().add(item.getOption1());					e.getQuestionnaireItemMap().put(item.getSubject(),item);				}else{					mapItem.getOptionList().add(item.getOption1());//为指定的题目添加选项				}			}		}				logger.error("e.getQuestionnaireItemMap() = " + e.getQuestionnaireItemMap());		return show;	}		/**	 * 用户提交问卷	 * @return	 * @throws IOException 	 */	@RequestMapping("/submitQuestion")	public String submitQuestion() throws IOException{		logger.error("submitQuestion...");		//		response.sendRedirect("success.html");//		return null;		return SUCCESS;	}		}