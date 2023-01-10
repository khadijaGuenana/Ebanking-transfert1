import React, { useState } from 'react'
import Logo from '../components/Logo'
import Header from '../components/Header'
import Button from '../components/Button'
import TextInput from '../components/TextInput'
import { passwordValidator } from '../helpers/passwordValidator'
import { ScrollView } from 'react-native-gesture-handler'
import {nameValidator} from '../helpers/nameValidator'
import { phoneValidator } from '../helpers/phoneValidator'
import { cneValidator } from '../helpers/cneValidator'
import { GlobalStyles } from '../styles/globalStyles'
import ActivityIndicator from '../styles/ActivityIndicator'
import FlashMessage,{ showMessage } from "react-native-flash-message";
import { View } from 'react-native';
import axios from 'axios';

export default function AddBeneficiaryScreen({ navigation,route }) {
  const [firstName, setFirstName] = useState({ value: '', error: '' })
  const [seconName, setSecondName] = useState({ value: '', error: '' })
  const [phone, setPhone] = useState({ value: '', error: '' })
  const [cne, setCne] = useState({ value: '', error: '' })
  const listBeneficiary = route.params.listBeneficiary
  const baseUrl = "http://192.168.43.39:8080/"

  const onAddPressed = async() => {
    const firstNameError = nameValidator(firstName.value)
    const secondNameError = nameValidator(seconName.value)
    const phoneError = phoneValidator(phone.value)
    const cneError = cneValidator(cne.value)

    if (firstNameError || secondNameError || phoneError || cneError) {
      setFirstName({ ...firstName, error: firstNameError })
      setSecondName({ ...seconName, error: secondNameError })
      setPhone({ ...phone, error: phoneError })
      setCne({ ...cne, error:cneError })
      return
    }
    let payload={
      firstName:firstName.value,
      secondName:seconName.value,
      phone:phone.value,
      cne:cne.value
    }
    payload = JSON.stringify(payload);
    try {
      let res = await axios({
        method: 'POST',
        url: baseUrl+"client/",
        data:payload,
        headers: {
          'Content-Type': 'application/json',
      }
      });
      if (res.status === 201) {
        const obj = {
          title:firstName.value+" "+seconName.value,
          id:res.data.id
        }
        //afficher data
        console.log(res.data)
        listBeneficiary.push(obj)
        navigation.navigate('AddTransferScreen',{listBeneficiary})
      }
    } catch (err) {
        console.log(err)
        showMessage({
          message: "Erreur",
          description: "Erreur s'est produite !!",
          type: "danger",
          icon:"danger"
        });
    }
    
  }

  return (   
    <ScrollView>
       <View style = {GlobalStyles.addBenScreen}>
        <Logo />
        <Header>Ajouter une bénéficiare.</Header>
        <TextInput
          label="Nom"
          returnKeyType="next"
          value={firstName.value}
          onChangeText={(text) => setFirstName({ value: text, error: '' })}
          error={!!firstName.error}
          errorText={firstName.error}
          autoCapitalize="none"
        />
        <TextInput
          label="Prénom"
          returnKeyType="next"
          value={seconName.value}
          onChangeText={(text) => setSecondName({ value: text, error: '' })}
          error={!!seconName.error}
          errorText={seconName.error}
          autoCapitalize="none"
        />
      <TextInput
          label="Téléphone"
          returnKeyType="next"
          value={phone.value}
          onChangeText={(text) => setPhone({ value: text, error: '' })}
          keyboardType='numeric'
          error={!!phone.error}
          errorText={phone.error}
          autoCapitalize="none"
        />
        <TextInput
          label="CNE"
          returnKeyType="next"
          value={cne.value}
          onChangeText={(text) => setCne({ value: text, error: '' })}
          error={!!cne.error}
          errorText={cne.error}
          autoCapitalize="none"
        />
        <Button mode="contained" onPress={onAddPressed}>
          Ajouter
        </Button>
        <FlashMessage
            position="top"
            style={{ marginVertical : 10 }}
            duration={2000}
        />
    </View>
    </ScrollView>
  )
}
