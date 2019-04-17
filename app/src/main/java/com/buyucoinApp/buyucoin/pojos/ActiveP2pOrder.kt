package com.buyucoinApp.buyucoin.pojos

import org.json.JSONArray

data class ActiveP2pOrder(
        var amount : Double ? = null,
        var boost: Double ? = null,
        var currency: Int ? = null,
        var duration: Int ? = null,
        var end_timestamp: String ? = null,
        var fee: Double ? = null,
        var filled: Double ? = null,
        var filled_by: String ? = null,
        var id: Int ? = null,
        var matched: Double ? = null,
        var matched_by: JSONArray ? = null,
        var matches: Any ? = null,
        var min_amount: Double ? = null,
        var modes: JSONArray ? = null,
        var upi_address: String ? = null,
        var pending: Boolean ? = null,
        var rejected_matches: Any ? = null,
        var status: Int ? = null,
        var timestamp: String ? = null,
        var tx_hash: String ? = null,
        var type: Int ? = null,
        var note: String ? = null,
        var wallet_id: Int ? = null,
        var user_id: Int ? = null,
        var wfee_amount: Int ? = null
)