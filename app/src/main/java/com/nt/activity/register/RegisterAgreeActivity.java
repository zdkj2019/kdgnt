package com.nt.activity.register;

import java.util.Vector;

import org.json.JSONObject;

import com.nt.R;
import com.nt.activity.FrameActivity;
import com.nt.utils.Config;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterAgreeActivity extends FrameActivity {

	private TextView tv_agreement;
	private EditText et_agreement;
	private CheckBox checkbox_agreement;
	private Button btn_next;
	private int type = 0;
	private int time = 15;
	private Intent intent;

	private String name = "";
	private String qyname = "";
	private String selfnum = "";
	private String address = "";
	private String phonenum = "";
	private String phonenum2 = "";
	private String province_id = "";
	private String city_id = "";
	private String area_id = "";
	private String ids = "";
	private byte[] photo_file2 = null;
	private byte[] photo_file1 = null;

	private String zbh_return = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_agree);
		initView();
		initListener();
	}

	protected void initView() {
		intent = getIntent();
		type = intent.getIntExtra("type", 0);

		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		et_agreement = (EditText) findViewById(R.id.et_agreement);
		checkbox_agreement = (CheckBox) findViewById(R.id.checkbox_agreement);
		btn_next = (Button) findViewById(R.id.btn_next);

		String xy = "<div>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<font style='font-weight: 600'>重庆正德科技股份有限公司</font>"
				+ "（以下简称“甲方”）做为“优+ESP”APP 软件的运营商和版权所有者，是一家合法注册的、在中国范围内开展以自助设备、金融机具为主的专业化服务企业。服务范围主要包括设备的安装调试、现场培训、现场风险排查、现场巡检、设备维护与维修、软件服务、网络服务等。"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;乙方作为 APP 软件平台的注册者（以下简称“乙方”），本身具备相关技术支撑服务能力，且自愿接受甲方及甲方最终用户在业务上的管理、指导、督查和安排，开展相关的服务实施和完成相关的服务任务。"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;经双方友好协商，达成本协议如下："
				+ "<br />"
				+ "<h4>第一条 协议描述</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.乙方承诺在乙方注册所在区县内，对甲方需其协助处理并分配的服务任务提供技术支撑服务，并在服务实施过程中全面接受甲方的统一管理和督导；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.本协议执行时间：自注册之日起，1 年内有效；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.协议到期前一个月，经双方协商可进行续签。如协商不成，本协议自动终止；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.鉴于本协议属于框架协议，在协议规定范围内，甲方根据业务需要分配乙方上门为甲方客户提供服务。因此，在协议有效期内，甲方不能保证一定有业务分配给乙方或有多少次数的业务分配给乙方；"
				+ "<br />"
				+ "<h4>第二条 维护服务内容</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.服务范围：以甲方正式发布的服务范围为准。包括但不限于自助设备（如缴费机等）、金融机具（如 POS 等）等设备的安装调试、现场培训、现场风险排查、现场巡检、设备维护与维修、软件服务、网络服务等；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.服务时限承诺、服务考核、服务流程等以甲方正式公布的为准；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.备件：如维修涉及备件，备件由甲方提供，但乙方有义务妥善保管和使用；"
				+ "<br />"
				+ "<h4>第三条 双方的权利和义务</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.未经甲、乙双方书面确认，任何一方无权对本协议条款进行任何修改、补充。如有修改或变更，经双方授权人签署作为本协议附件；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.甲乙双方在签订协议时应提供必要的证明文件（包括乙方的营业执照、乙方法人或授权签约人的身份证复印件、乙方用于结算费用的银行卡复印件）；"
				+ "<br />"
				+ "<br />"
				+ "甲方权责："
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.甲方有权要求乙方按照甲方的服务流程和标准开展维护工作，同时甲方有权对乙方的服务过程质量和服务时效进行管理和考核；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.甲方有权对乙方的维修技能进行培训和考核，乙方未通过考核前，甲方有权不派发任何服务支持任务给乙方；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. 甲方有权根据实际情况对公布的各类服务规范、考核标准、价格等进行调整，同时甲方有义务在调整前正式通知乙方。如乙方拒绝执行，经协商后仍无法达成一致，甲方有权提出终止本协议，且甲方无需向乙方支付任何形式的赔偿；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4. 甲方有权要求乙方在终止协议前 1 个月提出进行必要的工作交接。如乙方拒绝配合，甲方有权终止本协议，要求乙方对拒绝进行必要工作交接而造成的损失进行赔偿；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.如甲方因业务发展需要停止运作“电子服务平台”，甲方提前 15 日（含）以邮件或书面的方式正式告知乙方后，甲方有权直接终止本协议，且甲方无需向乙方支付任何形式的赔偿；"
				+ "<br />"
				+ "<br />"
				+ "乙方权责："
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.乙方有权利在维护协议范围内工作。但乙方不得把本协议约定设备种类和服务地域范围内的维修服务委托第三方维修，否则甲方有权终止本协议，并要求乙方赔偿相关损失；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.乙方有义务在设备维护期内，严格按照本协议规定的服务内容，接受甲方的统一督导和管理，做好服务支持任务实施工作；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.乙方有义务派出合格的工作人员提供服务，并保证人员的素质、服务质量满足甲方及最终用户的要求；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.乙方有义务采取足够的安全措施保障自身及相关工作人员的人身安全，并对此负全部责任，甲方不承担任何赔偿责任。如果乙方及乙方相关工作人员在为甲方服务期间，由于出现以下情况包括但不限于：身体不适、因服务现场环境、设备可能导致影响乙方及乙方相关工作人员的人身安全等，乙方可以终止服务，并且无需承担任何赔偿责任。但乙方应在条件允许的情况下第一时间向甲方进行报备；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.乙方及乙方工作人员由于服务质量、服务技能、服务态度不达标导致甲方被客户投诉或出现其他影响甲方经营、声誉的情况，甲方有权终止本协议并不支付任何赔偿；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.乙方出现各类造假行为（如私自售卖零部件、虚造故障等）经甲方查实后，甲方有权直接终止本协议，同时甲方有权追回全部乙方造假所得收入，并要求乙方按照造假收入的 5 倍进行赔偿；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.乙方有义务保护甲方客户的信息安全、设备安全、人身安全及现场环境良好。严禁乙方出现利用工作便利侵害甲方及甲方客户利益的不安全行为；"
				+ "<br />"
				+ "<h4>第四条 价格及结算</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. 服务按次结算，甲方对需要乙方上门服务的需求按次发包给乙方。双方根据服务种类、对应价格进行结算。具体的价格标准按照甲方正式公布的价格规定为准；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. 付款周期： 甲方按 月 结算给乙方：双方根据“电子服务平台”数据统计确认，按自然月结算服务费用；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. 费用确认及支付："
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（1）乙方按照甲方规定的流程确认费用；具体流程以甲方公布的为准；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（2）甲方对电子服务平台数据建立了严格的数据内控管理制度，避免数据失真。同时，甲方本着对双方负责的态度，将对费用进行多次确认，确保不出现费用误差；如出现费用无法确认或费用异常，为保障乙方利益，请乙方在完工后 72 小时内联系甲方的对口联络人进行确认，避免出现费用错误；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（3）甲方次月 20 日前完成费用确认与付款；付款通过甲方和乙方银行帐户以划帐的方式支付；如果出现特殊情况导致无法按时付款，甲方将提前告知乙方并解释原因，如甲方需要更多的时间和流程来核实，甲方可能出现再次延迟付款,但原则上自延迟之日起不超过 1 个月；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（4）乙方应向甲方提供甲方要求的银行卡复印件以便付款。如乙方需改变账户，应提前十日以书面通知甲方，避免无法按时收到服务费用；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（5）如果因为系统 BUG 等原因导致数据丢失或统计失真，如果涉及历史收款差错，双方经协商后按照实际的情况结算，多退少补；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（6）当甲方累计支付乙方的费用金额超过 1000 元时，如乙方具备提供正式发票的资质，乙方应向甲方提供正式的发票（发票科目为服务费、维护费、电脑维护费均可）；"
				+ "<br />"
				+ "<h4>第五条 信息安全及竞业禁止</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. 保密规定："
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（1） 双方应当对本协议的内容、因履行本协议或在本协议期间获得的或收到的对方的商务、财务、技术、产品的信息、用户资料或其他标明保密的文件或信息的内容(简称“保密资料”)保守秘密，未经信息披露方书面事先同意，不得向本协议以外的任何第三方披露。资料接受方可仅为本协议目的向其确有知悉必要的雇员披露对方提供的保密资料，但同时须指示其雇员遵守本条规定的保密及不披露义务。双方应仅为本协议目的而复制和使用保密资料；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（2） 除非得到另一方的书面许可，甲乙双方均不得将本合同中的内容及在本合同执行过程中获得的对方的商业信息向任何第三方泄露；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（3）本保密义务应在本协议期满、解除或终止后，3 年内仍然有效；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（4）如乙方或乙方参与、接触本协议、执行现场服务的相关人员违反本协议所规定的保密条款，甲方有权要求乙方赔偿由于乙方泄密而为甲方造成的所有一切损失。如乙方泄密涉及到违反国家相关的法律法规，甲方保留追究乙方相关刑事、民事责任的权利；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. 信息安全："
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（1）乙方及乙方相关工作人员到场服务，涉及到资金安全、信息安全、交易安全需严格按照甲方服务规定与要求执行，乙方应采取措施确保甲方的信息安全、甲方客户的信息安全，不得出现盗取客户信息、安装不良软件等危害金融安全、业务安全的行为。如乙方违规操作造成而导致的刑事、民事及经济责任由乙方全部承担，与甲方无关；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（2）在本协议的执行过程中以及协议执行完成后 1 年内，乙方不得直接向甲方的客户接包本协议范围内的设备技术支撑服务，否则甲方有权单方终止本协议，并要求违约方赔偿由此所受一切损失。具体的赔偿标准为：乙方按照本协议期限内甲方支付乙方总金额的 3 倍金额赔偿给甲方；"
				+ "<br />"
				+ "<h4>第六条 不可抗力</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.在本协议的执行过程中，如出现不可抗力（如地震、洪水、火灾），受不可抗力影响的一方应尽快将所发生的不可抗力以书面形式通知对方，同时采取有效措施尽量减少因事故影响所造成的损失，双方应通过协商解决本协议的继续执行等问题；"
				+ "<br />"
				+ "<h4>第七条 特别规定</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.乙方的服务范围包含有电子电路、强电范畴，甲方提醒乙方：为了保障乙方自身的利益，乙方派出的技术人员需按照国家的相关要求取得相关上岗证书，同时按照国家相关的电子电路、强电安全操作流程执行（如强电必须断电操作等规定）。如由于乙方技术人员未按照国家相关安全操作流程和标准执行、或不具备相关证书、证书过期或遗失后不办理相关登记等所导致的一切安全意外、事故由乙方自己承担全部责任，甲方不承担任何赔偿责任；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.乙方及乙方技术人员需严格遵守国家的相关法律法规，严禁从事任何违反法律法规或侵犯任何第三方合法权益的行为，如违反本条约定，导致任何第三方向甲方提出索赔、异议、禁止或其他权利请求，乙方将自行承担费用提供抗辩并承担因此而产生的所有赔偿、罚款、合理律师费和损害赔偿。由此给甲方造成声誉或经济损失的，由乙方承担赔偿责任；"
				+ "<br />"
				+ "<h4>第八条 争议处理</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.由于违约、协议终止和协议有效期等与本协议有关事宜引起争议，各方应首先力求以友好协商的方式予以解决。如果在一方提交书面通知要求另一方开始友好协商后 20 天内，此争议仍未解决，则需提请重庆市渝中区仲裁委员会仲裁；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.在争议处理过程中，除正在协商或仲裁的部分外，协议的其它部分继续执行（特别是关于安全和保密的条款在争议过程中也继续有效）；"
				+ "<br />"
				+ "<h4>第九条 其他</h4>"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. 本协议经乙方确认后正式生效；"
				+ "<br />"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.本协议如有部分条款与国家相关法律法规抵触，一切以国家的法律法规为准。但本协议中其他符合国家法律法规的条款仍然有效；"
				+ "<br /> 以下无正文。"
				+ "<br />" + "</div>";
		
		if (type == 1) {
			tv_agreement.setText("个人版协议");
			et_agreement.setText(Html.fromHtml(xy));
		} else if (type == 2) {
			tv_agreement.setText("企业版协议");
			et_agreement.setText(Html.fromHtml(xy));
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (time > 0) {
						time = time - 1;
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initListener() {
	}

	private void submitData() {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					selfnum = selfnum.toLowerCase();
					String sql = "insert into USER_PASSWORD_2 (userid,username,whcd,sfzh,jzdz,sjid,sjhm,id,rylb,gzjl,password,zyshgx,lzsj,byyx,zzmm,wydj,jsjdj) "
							+ "values ("
							+ "'%s',"
							+ "'"
							+ name
							+ "','"
							+ qyname
							+ "','"
							+ selfnum
							+ "','"
							+ address
							+ "',"
							+ "to_number('0')"
							+ ",'"
							+ phonenum
							+ "',to_number('%s'),'"
							+ type
							+ "','1','"
							+ selfnum.substring(selfnum.length() - 6,
									selfnum.length())
							+ "','"
							+ ids
							+ "',sysdate,'"+phonenum2+"','"+province_id+"','"+city_id+"','"+area_id+"')";

					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					String flag = object.getString("flag");
					zbh_return = object.getString("zbh");

					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 2;// 完成
						handler.sendMessage(msg);

					} else {
						Message msg = new Message();
						msg.what = 4;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 4;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void returnData() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String sql = "delete from  USER_PASSWORD_2 where userid = '"
							+ zbh_return + "'";
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					Message msg = new Message();
					msg.what = 5;//
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 5;//
					handler.sendMessage(msg);
				}

			}
		});
	}

	private void upload() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (photo_file1 != null) {
						boolean flag = uploadPic("", photo_file1,
								"uf_json_setdata");
						if (flag) {
							if (photo_file2 != null) {
								flag = uploadPic("", photo_file2,
										"uf_json_setdata");
								if (flag) {
									Message msg = new Message();
									msg.what = 3;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = 0;
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 3;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}

		});

	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_ESP_ZCMX", "0001", zbh_return + "*1", "0001",
					data1, "uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				returnData();
				break;
			case 1:
				if (time == 0) {
					btn_next.setText("下一步");
					btn_next.setBackgroundResource(R.drawable.btn_normal);
					btn_next.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (checkbox_agreement.isChecked()) {
								qyname = intent.getStringExtra("qyname");
								name = intent.getStringExtra("name");
								selfnum = intent.getStringExtra("selfnum");
								ids = intent.getStringExtra("ids");
								address = intent.getStringExtra("address");
								phonenum = intent.getStringExtra("phonenum");
								phonenum2 = intent.getStringExtra("phonenum2");
								province_id = intent.getStringExtra("province_id");
								city_id = intent.getStringExtra("city_id");
								area_id = intent.getStringExtra("area_id");
								photo_file2 = intent
										.getByteArrayExtra("photo_file2");
								photo_file1 = intent
										.getByteArrayExtra("photo_file1");
								submitData();
							} else {
								Toast.makeText(getApplicationContext(),
										"请同意本协议！", 1).show();
							}

						}
					});
				} else {
					btn_next.setText(time + "秒");
				}

				break;
			case 2:
				upload();
				break;
			case 3:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Intent intent = new Intent(getApplicationContext(),
						RegisterCompleteActivity.class);
				intent.putExtra("zbh", zbh_return);
				startActivity(intent);
				break;
			case 4:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("注册失败,请检查网络连接是否正常！", null);
				break;
			case 5:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("注册失败,上传图片失败！", null);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}
}
