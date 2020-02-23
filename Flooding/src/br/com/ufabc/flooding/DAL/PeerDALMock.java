package br.com.ufabc.flooding.DAL;

import br.com.ufabc.flooding.model.Peer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PeerDALMock {
    private HashMap<Integer, Peer> peerMap;
    
	public PeerDALMock() {
    	Peer peer1 = new Peer("Peer 1", 9873, Arrays.asList(2, 3));
    	Peer peer2 = new Peer("Peer 2", 9874, Arrays.asList(1, 4, 5));
    	Peer peer3 = new Peer("Peer 3", 9875, Arrays.asList(1, 6, 7));
    	Peer peer4 = new Peer("Peer 4", 9876, Arrays.asList(2, 8, 9));
    	Peer peer5 = new Peer("Peer 5", 9877, Arrays.asList(10, 2));
    	Peer peer6 = new Peer("Peer 6", 9878, Arrays.asList(3));
    	Peer peer7 = new Peer("Peer 7", 9879, Arrays.asList(3));
    	Peer peer8 = new Peer("Peer 8", 9880, Arrays.asList(4));
    	Peer peer9 = new Peer("Peer 9", 9881, Arrays.asList(4));
    	Peer peer10 = new Peer("Peer 10",9882, Arrays.asList(5));
    	
    	peerMap = new HashMap<Integer, Peer>(){
    			{
    				put(1, peer1);
    				put(2, peer2);
    				put(3, peer3);
    				put(4, peer4);
    				put(5, peer5);
    				put(6, peer6);
    				put(7, peer7);
    				put(8, peer8);
    				put(9, peer9);
    				put(10, peer10);
    			}
    		}; 
    }
	
	public Peer genereteNewPeer(int peerNumber) {
		return peerMap.get(peerNumber);
	}
   
}