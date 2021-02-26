/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi_interpolacao;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author gabri
 */
public class PI_Interpolacao {
    
    BufferedImage abreImagem() throws IOException{
        BufferedImage image = ImageIO.read(new File("Imagens-imput\\1.jpg"));
        return image;
        
    }
    public int[][] Image_To_Matriz(BufferedImage ImagemCarregada){
        int altura, largura;
        altura = ImagemCarregada.getHeight();
        largura = ImagemCarregada.getWidth();
        int vetAuxPixel[] = new int[largura*altura];
        vetAuxPixel = ImagemCarregada.getRGB(0, 0, largura, altura, null, 0, largura);
        int matrizPixel[][] = new int[altura][largura];
        int count = 0;
        for (int i=0;i<altura;i++)
        {
            for (int j=0;j<largura;j++)
            {
                matrizPixel[i][j]= vetAuxPixel[count];
                count++;
            }
        }
        return matrizPixel;
    }
    
    public void Cria_Imagem_Alterada(int [][] MatrizPixel,String NomeImagem) throws IOException{
        int new_altura,new_largura;
        new_altura=MatrizPixel.length;
        new_largura=MatrizPixel[1].length;
        int[] vetAux= new int[new_largura*new_altura];
        int count=0;
        for (int i=0;i<new_altura;i++){
            for(int j=0;j<new_largura;j++){
                vetAux[count]=MatrizPixel[i][j];
                count++;
            }
        }
        BufferedImage new_image_altera = new BufferedImage(new_largura, new_altura,BufferedImage.TYPE_INT_RGB);
        new_image_altera.setRGB(0, 0, new_largura, new_altura, vetAux, 0, new_largura);
        ImageIO.write(new_image_altera,"JPG", new File(NomeImagem+".jpg"));
    }
    
    public void InterpolacaoVizinhoAmplia(int [][]MatrizPixelOld) throws IOException{
        int altura,largura;
        altura = MatrizPixelOld.length;
        largura = MatrizPixelOld[1].length;
        int[][] MatrizNOVA = new int[altura+(altura-1)][largura+(largura-1)];
        int aux_h=0,aux_w=0;
        for (int i=0;i<MatrizNOVA.length;i=i+2){
            aux_w=0;
            for (int j=0;j<MatrizNOVA[1].length;j=j+2){
                MatrizNOVA[i][j]=MatrizPixelOld[aux_h][aux_w];
                aux_w++;
            }
            aux_h++;
        }
        for (int i=0;i<MatrizNOVA.length-1;i=i+2){
            for (int j=0;j<MatrizNOVA[1].length-1;j=j+2){
                MatrizNOVA[i][j+1]=MatrizNOVA[i][j];
                MatrizNOVA[i+1][j]=MatrizNOVA[i][j];
                MatrizNOVA[i+1][j+1]=MatrizNOVA[i][j];
            }
        }
        Cria_Imagem_Alterada(MatrizNOVA, "VizinhoAmpliado");
    }
    
    public void InterpolacaoVizinhoReducao(int[][] MatrizPixelOld) throws IOException{
        int altura,largura;
        altura = MatrizPixelOld.length;
        largura = MatrizPixelOld[1].length;
        int[][] MatrizNova = new int[altura/2][largura/2];
        int aux_h=0,aux_w=0;
        for(int i=0;i<MatrizNova.length;i++)
        {
            aux_w=0;
            for(int j=0;j<MatrizNova[1].length;j++){
                MatrizNova[i][j]=MatrizPixelOld[aux_h][aux_w];
                aux_w=aux_w+2;
            }
            aux_h=aux_h+2;
        }
        Cria_Imagem_Alterada(MatrizNova, "VizinhoReduzida");
    }
    
    public void InterpolacaoBilinearAmpliacao(BufferedImage ImagemCarregada) throws IOException{ 
        int altura, largura;
        altura = ImagemCarregada.getHeight();
        largura = ImagemCarregada.getWidth();
        int matrizPixel[][] = new int[altura+(altura-1)][largura+(largura-1)];
        int count = 0;
        int[] VetRed = new int[largura*altura];
        int[] VetGreen = new int[largura*altura];
        int[] VetBlue = new int[largura*altura];
        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++){
                Color color = new Color(ImagemCarregada.getRGB(j, i));
                VetRed[count]=color.getRed();
                VetBlue[count]=color.getBlue();
                VetGreen[count]=color.getGreen();
                count++;
            }
        }
        count=0;
        int [][] matrizRed = new int[altura+(altura-1)][largura+(largura-1)];
        int [][] matrizGreen = new int[altura+(altura-1)][largura+(largura-1)];
        int [][] matrizBlue = new int[altura+(altura-1)][largura+(largura-1)];
        for(int i=0;i<matrizBlue.length;i=i+2){
            for(int j=0;j<matrizBlue[1].length;j=j+2){
                matrizRed[i][j]=VetRed[count];
                matrizGreen[i][j]=VetGreen[count];
                matrizBlue[i][j]=VetBlue[count];
                count++;
            }
        }
        int h=0,w=0;
        for(int i=0;i<matrizPixel.length-1;i=i+2){
            w=0;
            for(int j=0;j<matrizPixel[1].length-1;j=j+2){
                
                matrizBlue[i][j+1]=((matrizBlue[h][w]+matrizBlue[h][w+1])/2)%256;
                matrizBlue[i+1][j]=((matrizBlue[h][w]+matrizBlue[h+1][w])/2)%256;
                matrizBlue[i+1][j+1]=((matrizBlue[h][w]+matrizBlue[h][w+1]+matrizBlue[h+1][w]+matrizBlue[h+1][w+1])/4)%256;
                matrizBlue[i+1][j+2]=((matrizBlue[h][w+1]+matrizBlue[h+2][w+2])/2)%256;
                matrizBlue[i+2][j+1]=((matrizBlue[h+2][w]+matrizBlue[h+2][w+2])/2)%256;
                matrizGreen[i][j+1]=((matrizGreen[h][w]+matrizGreen[h][w+1])/2)%256;
                matrizGreen[i+1][j]=((matrizGreen[h][w]+matrizGreen[h+1][w])/2)%256;
                matrizGreen[i+1][j+1]=((matrizGreen[h][w]+matrizGreen[h][w+1]+matrizGreen[h+1][w]+matrizGreen[h+1][w+1])/4)%256;
                matrizGreen[i+1][j+2]=((matrizGreen[h][w+1]+matrizGreen[h+2][w+2])/2)%256;
                matrizGreen[i+2][j+1]=((matrizGreen[h+2][w]+matrizGreen[h+2][w+2])/2)%256;
                matrizRed[i][j+1]=((matrizRed[h][w]+matrizRed[h][w+1])/2)%256;
                matrizRed[i+1][j]=((matrizRed[h][w]+matrizRed[h+1][w])/2)%256;
                matrizRed[i+1][j+1]=((matrizRed[h][w]+matrizRed[h][w+1]+matrizRed[h+1][w]+matrizRed[h+1][w+1])/4)%256;
                matrizRed[i+1][j+2]=((matrizRed[h][w+1]+matrizRed[h+2][w+2])/2)%256;
                matrizRed[i+2][j+1]=((matrizRed[h+2][w]+matrizRed[h+2][w+2])/2)%256;
                w=w+2;
            }
            h=h+2;
        }
        BufferedImage aux = new BufferedImage(matrizPixel[1].length, matrizPixel.length, BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<matrizPixel.length;i++){
            for(int j=0;j<matrizPixel[1].length;j++){
                Color color = new Color(matrizRed[i][j], matrizGreen[i][j], matrizBlue[i][j]);
                aux.setRGB(j, i, color.getRGB());
            }
        }
       
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "BilinearAmpliado");
    }
    
    public void InterpolacaoBilinearReducao(BufferedImage imagemCarregada) throws IOException{
        int altura,largura;
        altura = imagemCarregada.getHeight();
        largura = imagemCarregada.getWidth();
        int[][] MatrizNova = new int[altura/2][largura/2];
        int[][] MatrizRed = new int[altura][largura];
        int[][] MatrizBlue = new int[altura][largura];
        int[][] MatrizGreen = new int[altura][largura];
        int[][] RedReduzida = new int[altura/2][largura/2];
        int[][] BlueReduzida = new int[altura/2][largura/2];
        int[][] GreeReduzida = new int[altura/2][largura/2];
        int aux_h=0,aux_w=0;
        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++)
            {
                Color color = new Color(imagemCarregada.getRGB(j, i));
                MatrizRed[i][j]=color.getRed();
                MatrizBlue[i][j]=color.getBlue();
                MatrizGreen[i][j]=color.getGreen();
            }
        }
        
        for(int i=0;i<MatrizNova.length;i++){
            aux_w=0;
            for(int j=0;j<MatrizNova[1].length;j++){
                RedReduzida[i][j]=((MatrizRed[aux_h][aux_w]+MatrizRed[aux_h][aux_w+1]+MatrizRed[aux_h+1][aux_w]+MatrizRed[aux_h+1][aux_w+1])/4)%256;
                BlueReduzida[i][j]=((MatrizBlue[aux_h][aux_w]+MatrizBlue[aux_h][aux_w+1]+MatrizBlue[aux_h+1][aux_w]+MatrizBlue[aux_h+1][aux_w+1])/4)%256;
                GreeReduzida[i][j]=((MatrizGreen[aux_h][aux_w]+MatrizGreen[aux_h][aux_w+1]+MatrizGreen[aux_h+1][aux_w]+MatrizGreen[aux_h+1][aux_w+1])/4)%256;
                aux_w=aux_w+2;
            }
            aux_h=aux_h+2;
        }
        BufferedImage aux = new BufferedImage(altura/2, largura/2, BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<MatrizNova.length;i++)
        {
            for(int j=0;j<MatrizNova[1].length;j++){
                Color color = new Color(RedReduzida[i][j],GreeReduzida[i][j],BlueReduzida[i][j]);
                aux.setRGB(j, i, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "BilinearReduzida");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        PI_Interpolacao Executar = new PI_Interpolacao();
        Executar.InterpolacaoBilinearReducao(Executar.abreImagem());
        Executar.InterpolacaoBilinearAmpliacao(Executar.abreImagem());
        Executar.InterpolacaoVizinhoReducao(Executar.Image_To_Matriz(Executar.abreImagem()));
        Executar.InterpolacaoVizinhoAmplia(Executar.Image_To_Matriz(Executar.abreImagem()));
        
        
    }
    
}
