//The MIT License (MIT)
//
//Copyright (c) 2014 LittleBoat
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
//www.littleboat.com.br


#import "CadastroViewController.h"
#import "LocalService.h"
#import "CadastroService.h"
#import "CheckInService.h"
#import "CheckInViewController.h"

@interface CadastroViewController ()

@property (weak, nonatomic) IBOutlet UIView *viewPicker;
@property (weak, nonatomic) IBOutlet UIButton *btnPais;
@property (weak, nonatomic) IBOutlet UIButton *btnEstado;
@property (weak, nonatomic) IBOutlet UIButton *btnCidade;
@property (weak, nonatomic) IBOutlet UIButton *btnBairro;
@property (weak, nonatomic) IBOutlet UIPickerView *pckLocal;
@property (weak, nonatomic) IBOutlet UIScrollView *scroll;
@property (weak, nonatomic) IBOutlet UIButton *btnParticipar;
@property (weak, nonatomic) IBOutlet UITextField *txtApelido;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UITextField *txtIdade;
@property (weak, nonatomic) IBOutlet UIImageView *imgBackground;
@property (weak, nonatomic) IBOutlet UIButton *btnHomem;
@property (weak, nonatomic) IBOutlet UIButton *btnMulher;
@property (strong, nonatomic) NSArray* paises;
@property (strong, nonatomic) NSArray* estados;
@property (strong, nonatomic) NSArray* cidades;
@property (strong, nonatomic) NSArray* bairros;
@property (strong, nonatomic) NSArray* dataSource;
@property (strong, nonatomic) NSString* idIbge;
@property (strong, nonatomic) NSString* idEstado;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) NSString* lat;
@property (strong, nonatomic) NSString* lng;

- (IBAction)btnPaisPressed:(UIButton *)sender;
- (IBAction)btnEstadoPressed:(UIButton *)sender;
- (IBAction)btnCidadePressed:(UIButton *)sender;
- (IBAction)btnBairroPressed:(UIButton *)sender;
- (IBAction)btnCancelPressed:(UIBarButtonItem *)sender;
- (IBAction)btnOkPressed:(UIBarButtonItem *)sender;
- (IBAction)btnParticiparPressed:(UIButton *)sender;
- (IBAction)btnSexPressed:(UIButton *)sender;
- (IBAction)btnVisualizarPressed:(id)sender;

@end

@implementation CadastroViewController

#pragma mark - Getters

-(NSArray *)paises{
    if(!_paises){
        _paises = [[NSArray alloc] initWithArray:[LocalService getCountry]];
    }
    return _paises;
}

-(NSArray *)estados{
    if(!_estados){
        _estados = [[NSArray alloc] initWithArray:[LocalService getStates]];
    }
    
    return _estados;
}

-(NSArray *)cidades{
    if(!_cidades){
        _cidades = [[NSArray alloc] initWithArray:[LocalService getCities: self.idEstado]];
    }
    return _cidades;
}

-(NSArray *)bairros{
    if(!_bairros){
        _bairros = [[NSArray alloc] initWithArray:[LocalService getBairros]];
    }
    return _bairros;
}

#pragma mark - View Methods

-(BOOL)canBecomeFirstResponder{
    return YES;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self becomeFirstResponder];
}

-(void)viewDidLoad{
    [super viewDidLoad];
    UITapGestureRecognizer* gesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTouch)];
    gesture.numberOfTapsRequired = 1;
    gesture.numberOfTouchesRequired = 1;
    [self.scroll addGestureRecognizer:gesture];
    
    self.locationManager = [[CLLocationManager alloc]init]; // Criação de objeto para obter o local
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.locationManager.delegate = self;
    [self.locationManager startUpdatingLocation];
    
    
    //Inicia com valores default
    self.idEstado = @"35";
    [self.btnCidade setTitle:@"SÃO PAULO" forState:UIControlStateNormal];
    [self.btnEstado setTitle:@"SP" forState:UIControlStateNormal];
    [self.btnPais setTitle:@"BRASIL" forState:UIControlStateNormal];
    
    self.btnCidade.enabled = YES;
    self.btnEstado.enabled = YES;
    self.btnBairro.enabled = YES;
    self.txtIdade.delegate = self;
}

-(void)handleTouch{
    [self becomeFirstResponder];
}

#pragma mark - IBAction de Botões de Local

- (IBAction)btnPaisPressed:(UIButton *)sender {
    [self becomeFirstResponder];
    self.dataSource = nil;
    self.btnParticipar.enabled = NO;
    //Abre picker
    [UIView animateWithDuration:0.33 animations:^{
        self.viewPicker.frame = CGRectMake(0, 0, self.viewPicker.frame.size.width, self.viewPicker.frame.size.height);
        [self.pckLocal selectRow:0 inComponent:0 animated:NO];
        self.dataSource = [NSArray arrayWithArray:self.paises];
        [self.pckLocal reloadAllComponents];
    }];
}

-(IBAction)btnEstadoPressed:(UIButton *)sender{
    [self becomeFirstResponder];
    self.dataSource = nil;
    self.btnParticipar.enabled = NO;
    //Abre picker
    [UIView animateWithDuration:0.33 animations:^{
        self.viewPicker.frame = CGRectMake(0, 0, self.viewPicker.frame.size.width, self.viewPicker.frame.size.height);
        [self.pckLocal selectRow:0 inComponent:0 animated:NO];
        self.dataSource = [NSArray arrayWithArray:self.estados];
        [self.pckLocal reloadAllComponents];
    }];
}

-(IBAction)btnCidadePressed:(UIButton *)sender{
    [self becomeFirstResponder];
    self.cidades = nil;
    self.dataSource = nil;
    self.btnParticipar.enabled = NO;
    //Abre picker
    [UIView animateWithDuration:0.33 animations:^{
        self.viewPicker.frame = CGRectMake(0, 0, self.viewPicker.frame.size.width, self.viewPicker.frame.size.height);
        [self.pckLocal selectRow:0 inComponent:0 animated:NO];
        self.dataSource = [NSArray arrayWithArray:self.cidades];
        [self.pckLocal reloadAllComponents];
    }];
}

-(IBAction)btnBairroPressed:(UIButton *)sender{
    [self becomeFirstResponder];
    self.dataSource = nil;
    self.btnParticipar.enabled = NO;
    self.dataSource = [NSArray arrayWithArray:self.bairros];
    //Abre picker
    [UIView animateWithDuration:0.33 animations:^{
        self.dataSource = [NSArray arrayWithArray:self.bairros];
        self.viewPicker.frame = CGRectMake(0, 0, self.viewPicker.frame.size.width, self.viewPicker.frame.size.height);
        [self.pckLocal selectRow:0 inComponent:0 animated:NO];
        [self.pckLocal reloadAllComponents];
    }];
}

#pragma mark - IBAction de Botões do Picker
- (IBAction)btnCancelPressed:(UIBarButtonItem *)sender {
//    self.dataSource = nil;
    [UIView animateWithDuration:0.33 animations:^{
        self.viewPicker.frame = CGRectMake(0, 568, self.viewPicker.frame.size.width, self.viewPicker.frame.size.height);
    }];
}

- (IBAction)btnOkPressed:(UIBarButtonItem *)sender {
    LocalModel* model = [self.dataSource objectAtIndex:[self.pckLocal selectedRowInComponent:0] ];
    //Selecionou Pais
    if([self.dataSource isEqual:self.paises]){
        [self.btnPais setTitle:model.name forState:UIControlStateNormal];
        if([model.name isEqualToString:@"BRASIL"] || [model.name isEqualToString:@" BRASIL"]){
            self.btnEstado.enabled = YES;
            self.btnParticipar.enabled = NO;
        }else{
            self.btnEstado.enabled = NO;
            self.btnParticipar.enabled = YES;
        }
        [self.btnEstado setTitle:@"--" forState:UIControlStateNormal];
        [self.btnCidade setTitle:@"Selecione sua Cidade" forState:UIControlStateNormal];
        [self.btnBairro setTitle:@"Selecione seu Bairro" forState:UIControlStateNormal];
        self.btnCidade.enabled = NO;
        self.btnBairro.enabled = NO;
    }
    // Selecionou Estado
    else if([self.dataSource isEqual:self.estados]){
        [self.btnEstado setTitle:model.name forState:UIControlStateNormal];
        if([model.name isEqualToString:@"SP"]){
            self.btnCidade.enabled = YES;
            self.btnParticipar.enabled = NO;
        }else{
            self.btnCidade.enabled = YES;
            self.btnParticipar.enabled = NO;
        }
        self.idEstado = model.idLocal;
        self.btnBairro.enabled = NO;
        [self.btnCidade setTitle:@"Selecione sua Cidade" forState:UIControlStateNormal];
        [self.btnBairro setTitle:@"Selecione seu Bairro" forState:UIControlStateNormal];
    }//Selecionou Cidade
    else if([self.dataSource isEqual:self.cidades]){
        [self.btnCidade setTitle:model.name forState:UIControlStateNormal];
        if([model.name isEqualToString:@"SÃO PAULO"] || [model.name isEqualToString:@" SÃO PAULO"]){
            self.btnBairro.enabled = YES;
            self.btnParticipar.enabled = NO;
        }else{
            self.btnBairro.enabled = NO;
            self.btnParticipar.enabled = YES;
        }
        [self.btnBairro setTitle:@"Selecione seu Bairro" forState:UIControlStateNormal];
    }//Selecionou Bairro
    else if([self.dataSource isEqual:self.bairros]){
        [self.btnBairro setTitle:model.name forState:UIControlStateNormal];
        self.btnParticipar.enabled = YES;

    }
    self.idIbge = model.idLocal;
    [self btnCancelPressed:sender];
}

#pragma mark - IBAction Botões de Cadastro

- (IBAction)btnParticiparPressed:(UIButton *)sender {
    CadastroService* service = [[CadastroService alloc] init];
    service.delegate = self;
    
    //Seleciona Sexo
    char sexo = 'F';
    if(sender == self.btnHomem){
        sexo = 'M';
    }
		
    //Validação de campos
    if(![self.txtEmail.text isEqualToString:@""] && self.txtEmail.text != nil && ![self validateEmail:self.txtEmail.text]){
			[[[UIAlertView alloc] initWithTitle:@"Cadastro" message:@"Formato de email inválido, por favor verifique seu email." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];

    }else{
		
		int idade = 0;
		if (![self.txtIdade.text isEqualToString:@""]) {
			idade = [self.txtIdade.text intValue];
		}
		//Chamada de webservice
        [service registerUserNickName:[self.txtApelido.text stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding] email:self.txtEmail.text sexo:sexo codigoIbge:self.idIbge lat:self.lat lng:self.lng withIdade:idade];
        sender.enabled = NO;
        
    }
    
}

- (IBAction)btnSexPressed:(UIButton *)sender {
    if(sender == self.btnHomem){
        self.btnHomem.selected = !self.btnHomem.isSelected;
        self.btnMulher.selected = NO;
    }else{
        self.btnHomem.selected = NO;
        self.btnMulher.selected = !self.btnMulher.isSelected;
    }
}

- (IBAction)btnVisualizarPressed:(id)sender {
	
	
}

//Validação de Email
- (BOOL) validateEmail: (NSString *) candidate {
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    
    return [emailTest evaluateWithObject:candidate];
}

#pragma mark - TextFieldDelegate
-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
	[textField resignFirstResponder];
	return TRUE;
}


-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
	
	
    //Validação de campo de idade
	if (textField.tag == 3) {
		
		if (![string isEqualToString:@"0"] &
			![string isEqualToString:@"1"] &
			![string isEqualToString:@"2"] &
			![string isEqualToString:@"3"] &
			![string isEqualToString:@"4"] &
			![string isEqualToString:@"5"] &
			![string isEqualToString:@"6"] &
			![string isEqualToString:@"7"] &
			![string isEqualToString:@"8"] &
			![string isEqualToString:@"9"] &
			![string isEqualToString:@""]
			) {
			return FALSE;
		}
		
		NSUInteger newLength = [textField.text length] + [string length] - range.length;
		return (newLength > 3) ? NO : YES;
	}
	
	
	return  TRUE;
}

#pragma mark - Cadastro Delegate
//Callback do webservice de cadastro
-(void)cadastroService:(CadastroService *)cadastroService didReturnId:(int)idCadastro{
    self.btnParticipar.enabled = YES;
    if(idCadastro != -1){
        
        //Salva no user defaults
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        [prefs setObject:[NSNumber numberWithInt:idCadastro] forKey:@"IDUser"];
        [prefs setObject:self.txtApelido.text forKey:@"Apelido"];
        [prefs setObject:self.lat forKey:@"Lat"];
        [prefs setObject:self.lng forKey:@"Lng"];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setLocale:[NSLocale systemLocale]];
        [dateFormat setDateFormat:@"dd/MM HH:mm"];
        [prefs setObject:[dateFormat stringFromDate: [NSDate date]] forKey:@"Data"];
        [prefs setObject:self.idIbge forKey:@"IBGE"];
        [prefs synchronize];
        [self performSegueWithIdentifier:@"home" sender:nil];
		
		[CheckInService setTime:nil];

    }else{
        [[[UIAlertView alloc] initWithTitle:@"Cadastro" message:@"Não foi possivel efetuar o cadastro, por favor tente de novo." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    //Chamada da tela de checkin
	if ([segue.identifier isEqualToString:@"goVisualizar"]) {
		CheckInViewController* vc = (CheckInViewController*)segue.destinationViewController;
		
		vc.isVisualizar = TRUE;
	}
}

#pragma mark - CLLocationDelegate

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation {
    [self.locationManager stopUpdatingLocation];
    self.lat = [NSString stringWithFormat: @"%f" ,newLocation.coordinate.latitude ];
    self.lng = [NSString stringWithFormat: @"%f" ,newLocation.coordinate.longitude ];
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error {
    [self.locationManager stopUpdatingLocation];
}

#pragma mark - UIPickerDelegate

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    LocalModel* model = [self.dataSource objectAtIndex:row];
    return model.name;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return [self.dataSource count];
}

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

@end
