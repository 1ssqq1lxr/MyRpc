package com.it.netty.rpc.message;

import java.io.Serializable;

public class Result implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String serialNo;
		private Object msg;
		private Exception exception;
		private String resultMsg;
		private String resultCode =Const.SUCCESS_CODE;
		
		public Result(Object msg, Exception exception, String resultMsg,
				String resultCode) {
			super();
			this.msg = msg;
			this.exception = exception;
			this.resultMsg = resultMsg;
			this.resultCode = resultCode;
		}
		
		
		public Result(String serialNo, Object msg, Exception exception,
				String resultMsg, String resultCode) {
			super();
			this.serialNo = serialNo;
			this.msg = msg;
			this.exception = exception;
			this.resultMsg = resultMsg;
			this.resultCode = resultCode;
		}


		public String getSerialNo() {
			return serialNo;
		}


		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}


		public Result(Object msg) {
			super();
			this.msg = msg;
		}
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
