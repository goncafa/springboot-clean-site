package cl.example.transbank.controller;

import cl.transbank.onepay.Onepay;
import cl.transbank.onepay.exception.AmountException;
import cl.transbank.onepay.exception.SignatureException;
import cl.transbank.onepay.exception.TransactionCommitException;
import cl.transbank.onepay.exception.TransactionCreateException;
import cl.transbank.onepay.model.Item;
import cl.transbank.onepay.model.ShoppingCart;
import cl.transbank.onepay.model.Transaction;
import cl.transbank.onepay.model.TransactionCreateResponse;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TransactionController {
    @RequestMapping("/transaction-create")
    @ResponseBody
    public String create() throws AmountException, TransactionCreateException, SignatureException, IOException {
        ShoppingCart shoppingCart = new ShoppingCart();
        Item zapatos = new Item();
        zapatos.setAmount(1000);
        zapatos.setQuantity(1);
        zapatos.setDescription("Par de zapatos rosados");
        shoppingCart.add(zapatos);

        Onepay.setIntegrationType(Onepay.IntegrationType.LIVE);
        Onepay.setApiKey("lHqG9fNgKNpkv-GbWH1wGbOsao1_9QwOISZfXWUW8Rg");
        Onepay.setSharedSecret("?P6OK6Z1&8UD?2MR$EB&GVZO&DRRZ?P!");

        TransactionCreateResponse transactionCreateResponse = Transaction.create(shoppingCart, Onepay.Channel.WEB);
        System.out.println(transactionCreateResponse);

        Map<String, Object> toJson = new HashMap<>();
        toJson.put("occ", transactionCreateResponse.getOcc());
        toJson.put("ott", transactionCreateResponse.getOtt());
        toJson.put("externalUniqueNumber", transactionCreateResponse.getExternalUniqueNumber());
        toJson.put("qrCodeAsBase64", transactionCreateResponse.getQrCodeAsBase64());
        toJson.put("issuedAt", transactionCreateResponse.getIssuedAt());
        toJson.put("amount", shoppingCart.getTotal());

        return new Gson().toJson(toJson);
    }

    @RequestMapping("/onepay-result")
    @ResponseBody
    public String commit(@RequestParam("occ") String occ,
                         @RequestParam("externalUniqueNumber") String externalUniqueNumber,
                         @RequestParam("status") String status) throws SignatureException, IOException, TransactionCommitException {
        if (null != status && !status.equalsIgnoreCase("PRE_AUTHORIZED")) {
            return "Error!!";
        }

        Transaction.commit(occ, externalUniqueNumber);

        return "Exito!";
    }
}
