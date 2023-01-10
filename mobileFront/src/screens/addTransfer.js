import React, { useState,useEffect } from 'react'
import { Text, FlatList, View } from 'react-native'
import { Picker } from '@react-native-picker/picker';
import Logo from '../components/Logo'
import Header from '../components/Header'
import Button from '../components/Button'
import TextInput from '../components/TextInput'
import { GlobalStyles } from '../styles/globalStyles'
import FlashMessage,{ showMessage } from "react-native-flash-message";
import { montantValidator } from '../helpers/montantValidator';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
export default function AddTransferScreen({ navigation,route }) {

    const baseUrl = "http://192.168.43.39:8080/"
    const [montant,setMontant]=useState({value:'',error:''});
    const [modeCost,setModeCost]=useState();
    const [userConnected,setUserConnected]=useState();
    const [modeReg,setModeReg]=useState();
    const [listBeneficiary,setListBeneficiary]=useState([

    ])
    const Item = ({ title }) => (
        <View style={GlobalStyles.itemTransfer}>
          <Text style={GlobalStyles.titleTransfer}>{title}</Text>
        </View>
      );
      const renderItem = ({ item }) => (
        <Item title={item.title} />
      );
    const  toAddBeneficiary = ()=> {
        navigation.navigate("AddBeneficiaryScreen",{listBeneficiary});
      }
      useEffect(async ()=>{ 
        try {
            const user = await AsyncStorage.getItem('user');
            await new Promise(resolve => { setTimeout(resolve, 10); });
            if(user != null){
              const currentUser = JSON.parse(user)
              setUserConnected(currentUser);
            }
          }catch(err){
            console.log(err);
          }
        if(route.params!= undefined){
            setListBeneficiary(route.params.listBeneficiary)
        }
      },[]);
    const onTransfertPressed=async ()=>{
        const montantError = montantValidator(montant.value)
        if(montantError){
            setMontant({ ...montant, error:montantError })
            return
        }
        if(listBeneficiary.length==0){
            showMessage({
                message: "Erreur",
                description: "Vous devez d'abord séléctionner un bénéficiaire",
                type: "danger",
                icon:"danger"
              });
        }else if(listBeneficiary.length == 1){
            let payload = {
                montant:montant.value,
                clientSrc:userConnected.client.id,
                clientDst:listBeneficiary[0].id,
                modeCost:modeCost,
                mode:modeReg
            }
            console.log(payload)
            payload = JSON.stringify(payload);
            try {
              let res = await axios({
                method: 'POST',
                url: baseUrl+"transferservice/transfer/transferByWallet",
                data:payload,
                headers: {
                    'Content-Type': 'application/json',
                }
              });
              if (res.status === 201) {
                showMessage({
                    message: "success",
                    description: "le transfert a bien été effectué",
                    type: "success",
                    icon:"success"
                  });
              }
            } catch (err) {
                showMessage({
                  message: "Erreur",
                  description: "Réssayer plus tard",
                  type: "danger",
                  icon:"danger"
                });
            }

        }else{
            let payload = []
            listBeneficiary.forEach(benef => {
                 var elem =  {montant:montant.value,
                                clientSrc:userConnected.client.id,
                                clientDst:benef.id,
                                modeCost:modeCost,
                                mode:modeReg
                            }
                payload.push(elem);
            });
            console.log(payload)
            payload = JSON.stringify(payload);
            try {
              let res = await axios({
                method: 'POST',
                url: baseUrl+"transferservice/transfer/transferByWalletMult",
                data:payload,
                headers: {
                    'Content-Type': 'application/json',
                }
              });
              if (res.status === 201) {
                showMessage({
                    message: "succes",
                    description: "les transferts ont bien été effectué",
                    type: "success",
                    icon:"success"
                  });
              }
            } catch (err) {
                showMessage({
                  message: "Erreur",
                  description: "Réssayer plus tard",
                  type: "danger",
                  icon:"danger"
                });
            }
        }
    }
    return(
            <View style={GlobalStyles.containerLogin}>
                <Logo />
                <Header>Ajouter un transfert.</Header>
                <TextInput
                    label="Montant"
                    returnKeyType="next"
                    keyboardType='numeric'
                    value={montant.value}
                    onChangeText={(text) => setMontant({ value: text, error: '' })}
                    error={!!montant.error}
                    errorText={montant.error}
                    autoCapitalize="none"
                />
                <Button mode="contained" onPress={toAddBeneficiary}>
                    Ajouter un bénéficiaire
                </Button>
                <FlatList
                    data={listBeneficiary}
                    renderItem={renderItem}
                    keyExtractor={item => item.id}
                />
                <Text style = {GlobalStyles.txt}>Mode de frais : </Text>
                <Picker
                    selectedValue={modeCost}
                    style={{ height: 50, width: 150 }}
                    onValueChange={(itemValue, itemIndex) => setModeCost(itemValue)}
                >
                    <Picker.Item label="Partagé" value="Partagé" />
                    <Picker.Item label="Destination" value="Destination" />
                    <Picker.Item label="Source" value="Source" />
                </Picker>
                <Text style={GlobalStyles.txt}>Mode de régulation : </Text>
                <Picker
                    selectedValue={modeReg}
                    style={{ height: 50, width: 150 }}
                    onValueChange={(itemValue, itemIndex) => setModeReg(itemValue)}
                >
                    <Picker.Item label="ByGab" value="ByGab" />
                    <Picker.Item label="ByAgent" value="ByAgent" />
                    <Picker.Item label="ToAccount" value="ToAccount" />
                </Picker>
                <Button mode="contained" onPress={onTransfertPressed}>
                    Envoyer le transfert
                </Button>
                <FlashMessage
                    position="top"
                    style={{ marginVertical : 10 }}
                    duration={2000}
                />
            </View>     

    )


}
