package com.it.netty.rpc.message;

public class Result {
		private Object msg;
		private Exception exception;
		private String resultMsg;
		private String resultCode =Const.SUCCESS_CODE;
		public Object getMsg() {
			return msg;
		}
		public void setMsg(Object msg) {
			this.msg = msg;
		}
		public Exception getException() {
			return exception;
		}
		public void setException(Exception exception) {
			this.exception = exception;
		}
		public String getResultMsg() {
			return resultMsg;
		}
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
		public String getResultCode() {
			return resultCode;
		}
		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}
		
}
