package com.it.netty.rpc.flow;




public class FlowRestrictWraper{
		private  FlowRestrict flowRestrict;
		public FlowRestrictWraper(FlowRestrict flowRestrict) {
			super();
			this.flowRestrict=flowRestrict;
		}
		public boolean doRestrict(){
			return flowRestrict.restrict();
		}
		public void doRelease(){
			flowRestrict.doRelease();
		}
}
