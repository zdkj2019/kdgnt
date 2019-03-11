package com.nt.receiver;

import android.content.Context;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

public class MzReceiver extends MzPushMessageReceiver{

	@Override
	public void onPushStatus(Context arg0, PushSwitchStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterStatus(Context arg0, RegisterStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubAliasStatus(Context arg0, SubAliasStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubTagsStatus(Context arg0, SubTagsStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnRegister(Context arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnRegisterStatus(Context arg0, UnRegisterStatus arg1) {
		// TODO Auto-generated method stub
		
	}

}
