import React, { useState } from 'react';
import { View, StyleSheet} from 'react-native';
import { DataTable } from 'react-native-paper';
import Button  from '../components/Button';
import FlashMessage,{ showMessage } from "react-native-flash-message";
import axios from 'axios';

export default function TransferDetailsScreen({navigation,route}) {
  const transferDetails = route.params.item
  const [status, setStatus] = useState(transferDetails.transfer.status);
  var date_transfer = new Date(transferDetails.transfer.transferDate);
  var date_expir = new Date(transferDetails.transfer.exprDate);
  var date_transfer_str = date_transfer.getFullYear() + "/" +
  ("0" + (date_transfer.getMonth()+1)).slice(-2) + "/" +
  ("0" + date_transfer.getDate()).slice(-2) + " à " +
  ("0" + date_transfer.getHours()).slice(-2) + ":" +
  ("0" + date_transfer.getMinutes()).slice(-2) ;
  var date_expir_str = date_expir.getFullYear() + "/" +
  ("0" + (date_expir.getMonth()+1)).slice(-2) + "/" +
  ("0" + date_expir.getDate()).slice(-2) + " à " +
  ("0" + date_expir.getHours()).slice(-2) + ":" +
  ("0" + date_expir.getMinutes()).slice(-2) ;
  const baseUrl = "http://192.168.43.39:8080/";
  const restorTransfer=async()=>{
    try {
    let res = await axios({
      method: 'PUT',
      url: baseUrl+"transferservice/transfer/restore/"+transferDetails.transfer.id
    });
    if (res.status === 202) {
      setStatus('Réstitué');
      showMessage({
        message: "succes",
        description: "Le transfert est restitué.",
        type: "success",
        icon:"success"
      });
    }
  } catch (err) {
    if(err.response.status==401){
      showMessage({
        message: "Erreur",
        description: "Le Transfert est deja restitué.",
        type: "danger",
        icon:"danger"
      });
    }else 
      showMessage({
        message: "Erreur de server",
        description: "Réssayer plus tard",
        type: "danger",
        icon:"danger"
      });
  }
  }
  return (
    <View style={styles.container}>
      <DataTable>
        <DataTable.Row>
          <DataTable.Cell>Monatnt:</DataTable.Cell>
          <DataTable.Cell style={{flex:2,fontWeight:'bold'}}>{transferDetails.transfer.montant}</DataTable.Cell>
        </DataTable.Row>

        <DataTable.Row>
          <DataTable.Cell>Source:</DataTable.Cell>
          <DataTable.Cell style={{flex:2}}>{transferDetails.clientSrc.firstName} {transferDetails.clientSrc.secondName}</DataTable.Cell>
        </DataTable.Row>

        <DataTable.Row>
          <DataTable.Cell>Destination:</DataTable.Cell>
          <DataTable.Cell style={{flex:2}}>{transferDetails.clientDst.firstName} {transferDetails.clientDst.secondName}</DataTable.Cell>
        </DataTable.Row>
        <DataTable.Row>
          <DataTable.Cell>Date de transfert:</DataTable.Cell>
          <DataTable.Cell style={{flex:1}}>{date_transfer_str}</DataTable.Cell>
        </DataTable.Row>
        <DataTable.Row>
          <DataTable.Cell>Date expérartion:</DataTable.Cell>
          <DataTable.Cell style={{flex:1}}>{date_expir_str}</DataTable.Cell>
        </DataTable.Row>
        <DataTable.Row>
          <DataTable.Cell>Status:</DataTable.Cell>
          <DataTable.Cell style={{flex:2}}>{status}</DataTable.Cell>
        </DataTable.Row>
      </DataTable>
      <Button mode="contained" onPress={restorTransfer}>
        Réstituer
      </Button>
      <FlashMessage
        position="top"
        style={{ marginVertical : 10 }}
        duration={2000}
      />
    </View>
    
  );
}

const styles = StyleSheet.create({
  container: {
    paddingTop: 50,
    paddingHorizontal: 30,
  },
});